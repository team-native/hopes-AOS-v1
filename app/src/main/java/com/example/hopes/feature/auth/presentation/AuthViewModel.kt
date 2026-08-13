package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.validation.isValidSchoolEmail
import com.example.hopes.core.validation.isValidSignupPassword
import com.example.hopes.core.validation.isValidUsername
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.LoginUseCase
import com.example.hopes.domain.usecase.SendSignupCodeUseCase
import com.example.hopes.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AuthEffect {
    data object Authenticated : AuthEffect
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sendSignupCodeUseCase: SendSignupCodeUseCase,
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthEffect>()
    val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

    /** 화면 이벤트로 입력값과 인증 요청을 처리한다. */
    fun onEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.EmailChanged -> updateSignupEmail(event.value)
            is AuthScreenEvent.PasswordChanged -> updateSignupPassword(event.value)
            is AuthScreenEvent.NameChanged -> updateSignupName(event.value)
            is AuthScreenEvent.DepartmentChanged -> updateState { copy(department = event.value) }
            is AuthScreenEvent.GenerationChanged -> updateSignupGeneration(event.value)
            is AuthScreenEvent.VerificationCodeChanged -> updateVerificationCode(event.value)
            AuthScreenEvent.LoginClicked -> login()
            AuthScreenEvent.SignUpClicked -> submitSignup()
            AuthScreenEvent.SendVerificationCodeClicked -> sendVerificationCode()
            AuthScreenEvent.SignUpRequested -> updateState { copy(authStep = AuthStep.SignUp) }
            AuthScreenEvent.LoginRequested -> updateState { copy(authStep = AuthStep.Login) }
            AuthScreenEvent.LoginDismissed -> updateState { copy(authStep = AuthStep.Guide) }
        }
    }

    /** 이메일 입력 시 학교 이메일 형식을 즉시 검사해 화면 상태에 반영한다. */
    private fun updateSignupEmail(email: String) {
        updateState {
            copy(
                email = email,
                requestError = null,
                signupValidation = signupValidation.copy(
                    isEmailTouched = true,
                    emailError = email.invalidSchoolEmailError(),
                ),
            )
        }
    }

    /** 이름 입력 시 필수 입력과 최대 길이를 즉시 검사해 화면 상태에 반영한다. */
    private fun updateSignupName(name: String) {
        updateState {
            copy(
                name = name,
                requestError = null,
                signupValidation = signupValidation.copy(
                    isNameTouched = true,
                    nameError = name.invalidUsernameError(),
                ),
            )
        }
    }

    /** 기수 선택 시 필수 선택 오류를 해제하거나 표시한다. */
    private fun updateSignupGeneration(generation: String) {
        updateState {
            copy(
                generation = generation,
                signupValidation = signupValidation.copy(
                    isGenerationTouched = true,
                    generationError = generation.requiredGenerationError(),
                ),
            )
        }
    }

    /** 비밀번호 입력 시 영문·숫자와 길이 조건을 즉시 검사해 화면 상태에 반영한다. */
    private fun updateSignupPassword(password: String) {
        updateState {
            copy(
                password = password,
                requestError = null,
                signupValidation = signupValidation.copy(
                    isPasswordTouched = true,
                    passwordError = password.invalidSignupPasswordError(),
                ),
            )
        }
    }

    /** 가입 클릭 시 모든 필드를 검사하고 유효할 때 회원가입 정보를 서버로 전송한다. */
    private fun submitSignup() {
        val currentState = _uiState.value
        val signupValidation = SignupValidationUiState(
            isEmailTouched = true,
            isNameTouched = true,
            isGenerationTouched = true,
            isPasswordTouched = true,
            isVerificationCodeTouched = true,
            emailError = currentState.email.invalidSchoolEmailError(),
            nameError = currentState.name.invalidUsernameError(),
            generationError = currentState.generation.requiredGenerationError(),
            passwordError = currentState.password.invalidSignupPasswordError(),
            verificationCodeError = currentState.verificationCode.invalidVerificationCodeError(),
        )

        updateState { copy(signupValidation = signupValidation) }

        if (signupValidation.hasError) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, requestError = null) }
            signUp(currentState)
        }
    }

    /** 인증번호 입력 시 숫자 여섯 자리만 유지하고 형식 오류를 갱신한다. */
    private fun updateVerificationCode(value: String) {
        val numericCode = value.filter(Char::isDigit).take(VERIFICATION_CODE_LENGTH)

        updateState {
            copy(
                verificationCode = numericCode,
                requestError = null,
                signupValidation = signupValidation.copy(
                    isVerificationCodeTouched = true,
                    verificationCodeError = numericCode.invalidVerificationCodeError(),
                ),
            )
        }
    }

    /** 번호 발송 클릭 시 유효한 학교 이메일로만 인증번호 발송 UseCase를 실행한다. */
    private fun sendVerificationCode() {
        val currentState = _uiState.value
        val emailError = currentState.email.invalidSchoolEmailError()
        updateState {
            copy(
                signupValidation = signupValidation.copy(
                    isEmailTouched = true,
                    emailError = emailError,
                ),
            )
        }

        if (emailError != null) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, requestError = null) }

            when (sendSignupCodeUseCase(currentState.email)) {
                is AppResult.Success -> updateState {
                    copy(
                        isLoading = false,
                        requestError = null,
                    )
                }
                else -> updateState {
                    copy(
                        isLoading = false,
                        requestError = AuthRequestError.SendVerificationCodeFailed,
                    )
                }
            }
        }
    }

    /** 인증번호 확인 후 가입 정보를 전송하고 성공 시 인증 완료 효과를 발행한다. */
    private suspend fun signUp(currentState: AuthUiState) {
        val request = SignUpRequest(
            email = currentState.email,
            username = currentState.name,
            password = currentState.password,
            passwordConfirm = currentState.password,
            verificationCode = currentState.verificationCode,
            major = currentState.department.toMajorCode(),
            cohort = currentState.generation.toCohort(),
        )

        when (signUpUseCase(request)) {
            is AppResult.Success -> {
                updateState { copy(isLoading = false) }
                _effect.emit(AuthEffect.Authenticated)
            }
            else -> updateState {
                copy(
                    isLoading = false,
                    requestError = AuthRequestError.SignUpFailed,
                )
            }
        }
    }

    /** 로그인 클릭 시 UseCase를 실행하고 결과를 UiState 또는 일회성 효과로 전달한다. */
    private fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            updateState { copy(requestError = null) }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, requestError = null) }
            when (loginUseCase(currentState.email, currentState.password)) {
                is AppResult.Success -> {
                    updateState { copy(isLoading = false) }
                    _effect.emit(AuthEffect.Authenticated)
                }
                else -> updateState { copy(isLoading = false) }
            }
        }
    }

    private fun updateState(transform: AuthUiState.() -> AuthUiState) {
        _uiState.value = _uiState.value.transform()
    }
}

private const val VERIFICATION_CODE_LENGTH = 6

/** 화면 표시용 학과명을 회원가입 API의 고정 코드로 변환한다. */
internal fun String.toMajorCode(): String? {
    return when (this) {
        "소프트웨어개발과" -> "SOFTWARE"
        "IoT과" -> "IOT"
        "AI과" -> "AI"
        else -> null
    }
}

/** 화면 표시용 기수에서 서버가 요구하는 숫자만 추출한다. */
internal fun String.toCohort(): Int? {
    return filter(Char::isDigit).toIntOrNull()
}

private fun String.invalidSchoolEmailError(): SignupInputError? {
    return if (isValidSchoolEmail()) null else SignupInputError.InvalidSchoolEmail
}

private fun String.invalidUsernameError(): SignupInputError? {
    return if (isValidUsername()) null else SignupInputError.InvalidUsername
}

private fun String.requiredGenerationError(): SignupInputError? {
    return if (isNotBlank()) null else SignupInputError.GenerationRequired
}

private fun String.invalidSignupPasswordError(): SignupInputError? {
    return if (isValidSignupPassword()) null else SignupInputError.InvalidPassword
}

private fun String.invalidVerificationCodeError(): SignupInputError? {
    return if (length == VERIFICATION_CODE_LENGTH) null else SignupInputError.InvalidVerificationCode
}
