package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.core.validation.isValidSchoolEmail
import com.example.hopes.core.validation.isValidSignupPassword
import com.example.hopes.core.validation.isValidUsername
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.usecase.ConfirmSignupVerificationUseCase
import com.example.hopes.domain.usecase.LoginUseCase
import com.example.hopes.domain.usecase.RequestPasswordResetUseCase
import com.example.hopes.domain.usecase.ResetPasswordUseCase
import com.example.hopes.domain.usecase.SendSignupVerificationUseCase
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
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val sendSignupVerificationUseCase: SendSignupVerificationUseCase,
    private val confirmSignupVerificationUseCase: ConfirmSignupVerificationUseCase,
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
            is AuthScreenEvent.VerificationCodeChanged -> updateSignupVerificationCode(event.value)
            AuthScreenEvent.LoginClicked -> login()
            AuthScreenEvent.SignUpClicked -> submitSignup()
            AuthScreenEvent.SignUpRequested -> updateState {
                copy(authStep = AuthStep.SignUp, errorMessage = null)
            }
            AuthScreenEvent.LoginRequested -> updateState {
                copy(authStep = AuthStep.Login, errorMessage = null)
            }
            AuthScreenEvent.LoginDismissed -> updateState {
                copy(authStep = AuthStep.Guide, errorMessage = null)
            }
            AuthScreenEvent.ForgotPasswordClicked -> updateState {
                copy(
                    authStep = AuthStep.PasswordResetRequest,
                    passwordResetEmail = passwordResetEmail.ifBlank { email },
                    isPasswordResetCodeSent = false,
                    errorMessage = null,
                    statusMessage = null,
                )
            }
            is AuthScreenEvent.PasswordResetEmailChanged -> updateState {
                // 이메일이 바뀌면 이전에 발송된 인증번호는 새 이메일에 유효하지 않으므로 발송 상태를 리셋한다.
                copy(
                    passwordResetEmail = event.value,
                    isPasswordResetCodeSent = false,
                    errorMessage = null,
                    statusMessage = null,
                )
            }
            is AuthScreenEvent.PasswordResetCodeChanged -> updateState {
                copy(passwordResetCode = event.value, errorMessage = null)
            }
            is AuthScreenEvent.PasswordResetNewPasswordChanged -> updateState {
                copy(passwordResetNewPassword = event.value, errorMessage = null)
            }
            AuthScreenEvent.PasswordResetRequestClicked -> requestPasswordReset()
            AuthScreenEvent.PasswordResetSubmitClicked -> resetPassword()
            AuthScreenEvent.PasswordResetBackClicked -> updateState {
                copy(
                    authStep = AuthStep.Login,
                    errorMessage = null,
                    statusMessage = null,
                    passwordResetCode = "",
                    passwordResetNewPassword = "",
                    isPasswordResetCodeSent = false,
                )
            }
            AuthScreenEvent.SendSignupVerificationClicked -> sendSignupVerificationCode()
        }
    }

    /** 로그인 클릭 시 UseCase를 실행하고 결과를 UiState 또는 일회성 효과로 전달한다. */
    private fun login() {
        val currentState = _uiState.value
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (loginUseCase(currentState.email, currentState.password)) {
                is AppResult.Success -> {
                    updateState { copy(isLoading = false) }
                    _effect.emit(AuthEffect.Authenticated)
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 이메일 입력 시 학교 이메일 형식을 즉시 검사해 화면 상태에 반영한다. */
    private fun updateSignupEmail(email: String) {
        updateState {
            copy(
                email = email,
                errorMessage = null,
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
                errorMessage = null,
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

    /** 비밀번호 입력 시 영문·숫자와 길이 조건을 즉시 검사한다. */
    private fun updateSignupPassword(password: String) {
        updateState {
            copy(
                password = password,
                errorMessage = null,
                signupValidation = signupValidation.copy(
                    isPasswordTouched = true,
                    passwordError = password.invalidSignupPasswordError(),
                ),
            )
        }
    }

    /** 인증번호 입력 시 숫자 여섯 자리만 유지하고 형식 오류를 갱신한다. */
    private fun updateSignupVerificationCode(value: String) {
        val numericCode = value.filter(Char::isDigit).take(VERIFICATION_CODE_LENGTH)

        updateState {
            copy(
                verificationCode = numericCode,
                errorMessage = null,
                signupValidation = signupValidation.copy(
                    isVerificationCodeTouched = true,
                    verificationCodeError = numericCode.invalidVerificationCodeError(),
                ),
            )
        }
    }

    /** 번호 발송 클릭 시 유효한 학교 이메일로만 회원가입 인증번호 발송 UseCase를 실행한다. */
    private fun sendSignupVerificationCode() {
        val currentState = _uiState.value
        val emailError = currentState.email.invalidSchoolEmailError()
        updateState {
            copy(
                signupValidation = signupValidation.copy(isEmailTouched = true, emailError = emailError),
            )
        }

        if (emailError != null) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (sendSignupVerificationUseCase(currentState.email)) {
                is AppResult.Success -> updateState { copy(isLoading = false) }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 가입 클릭 시 모든 필드를 검사하고 유효할 때 회원가입 정보를 서버로 전송한다. */
    private fun submitSignup() {
        val currentState = _uiState.value
        val validation = SignupValidationUiState(
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

        updateState { copy(signupValidation = validation) }

        if (validation.hasError) {
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }

            // "회원가입" 클릭 한 번으로 인증번호 확인과 최종 가입 요청을 순서대로 처리한다.
            // 인증번호 확인이 실패하면 나머지 입력값은 전송하지 않는다.
            when (confirmSignupVerificationUseCase(currentState.email, currentState.verificationCode)) {
                is AppResult.Success -> Unit
                else -> {
                    updateState { copy(isLoading = false, errorMessage = "") }
                    return@launch
                }
            }

            // 회원가입 화면에는 별도의 비밀번호 확인 입력칸이 없어, 같은 비밀번호 값을 확인란으로도 함께 전송한다.
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
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 학교 이메일로 비밀번호 재설정 인증번호 발송을 요청한다. */
    private fun requestPasswordReset() {
        val email = _uiState.value.passwordResetEmail
        if (email.isBlank()) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null, statusMessage = null) }
            when (requestPasswordResetUseCase(email)) {
                is AppResult.Success -> updateState {
                    copy(isLoading = false, statusMessage = "", isPasswordResetCodeSent = true)
                }
                else -> updateState {
                    copy(isLoading = false, errorMessage = "", isPasswordResetCodeSent = false)
                }
            }
        }
    }

    /** 인증번호와 새 비밀번호를 서버에 제출해 비밀번호 재설정을 완료하고, 성공하면 로그인 단계로 되돌아간다. */
    private fun resetPassword() {
        val currentState = _uiState.value
        // 번호 발송이 성공한 뒤에만 재설정을 제출할 수 있도록 UI 게이팅과 동일한 조건을 한 번 더 검사한다.
        val isValid = currentState.isPasswordResetCodeSent &&
            currentState.passwordResetCode.isNotBlank() &&
            currentState.passwordResetNewPassword.isNotBlank()

        if (!isValid) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            // 비밀번호 재설정 화면에도 별도의 비밀번호 확인 입력칸이 없어, 같은 비밀번호 값을 확인란으로도 함께 전송한다.
            when (
                resetPasswordUseCase(
                    email = currentState.passwordResetEmail,
                    code = currentState.passwordResetCode,
                    password = currentState.passwordResetNewPassword,
                    passwordConfirm = currentState.passwordResetNewPassword,
                )
            ) {
                is AppResult.Success -> updateState {
                    copy(
                        isLoading = false,
                        authStep = AuthStep.Login,
                        passwordResetEmail = "",
                        passwordResetCode = "",
                        passwordResetNewPassword = "",
                        isPasswordResetCodeSent = false,
                        statusMessage = null,
                    )
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    private fun updateState(transform: AuthUiState.() -> AuthUiState) {
        _uiState.value = _uiState.value.transform()
    }
}

private const val VERIFICATION_CODE_LENGTH = 6

/** 화면 표시용 학과명을 회원가입 API의 고정 코드로 변환한다. */
private fun String.toMajorCode(): String? {
    return when (this) {
        "소프트웨어개발과" -> "SOFTWARE"
        "IoT과" -> "IOT"
        "AI과" -> "AI"
        else -> null
    }
}

/** 화면 표시용 기수에서 서버가 요구하는 숫자만 추출한다. */
private fun String.toCohort(): Int? {
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
