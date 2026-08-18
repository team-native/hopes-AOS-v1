package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            is AuthScreenEvent.EmailChanged -> updateState { copy(email = event.value, errorMessage = null) }
            is AuthScreenEvent.PasswordChanged -> updateState { copy(password = event.value, errorMessage = null) }
            is AuthScreenEvent.NameChanged -> updateState { copy(name = event.value, errorMessage = null) }
            is AuthScreenEvent.VerificationCodeChanged -> updateState {
                copy(verificationCode = event.value, errorMessage = null)
            }
            is AuthScreenEvent.PasswordConfirmChanged -> updateState {
                copy(passwordConfirm = event.value, errorMessage = null)
            }
            AuthScreenEvent.LoginClicked -> login()
            AuthScreenEvent.SignUpClicked -> signUp()
            is AuthScreenEvent.SignUpRequested -> updateState {
                copy(
                    authStep = AuthStep.SignUpEmailVerification,
                    email = email.ifBlank { event.sampleEmail },
                    name = name.ifBlank { event.sampleName },
                    errorMessage = null,
                )
            }
            AuthScreenEvent.LoginRequested -> updateState { copy(authStep = AuthStep.Login) }
            AuthScreenEvent.LoginDismissed -> updateState { copy(authStep = AuthStep.Guide) }
            AuthScreenEvent.ForgotPasswordClicked -> updateState {
                copy(
                    authStep = AuthStep.PasswordResetRequest,
                    passwordResetEmail = passwordResetEmail.ifBlank { email },
                    errorMessage = null,
                    statusMessage = null,
                )
            }
            is AuthScreenEvent.PasswordResetEmailChanged -> updateState {
                copy(passwordResetEmail = event.value, errorMessage = null, statusMessage = null)
            }
            is AuthScreenEvent.PasswordResetCodeChanged -> updateState {
                copy(passwordResetCode = event.value, errorMessage = null)
            }
            is AuthScreenEvent.PasswordResetNewPasswordChanged -> updateState {
                copy(passwordResetNewPassword = event.value, errorMessage = null)
            }
            is AuthScreenEvent.PasswordResetNewPasswordConfirmChanged -> updateState {
                copy(passwordResetNewPasswordConfirm = event.value, errorMessage = null)
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
                    passwordResetNewPasswordConfirm = "",
                )
            }
            AuthScreenEvent.SendSignupVerificationClicked -> sendSignupVerification()
            AuthScreenEvent.SignUpEmailVerificationBackClicked -> updateState {
                copy(authStep = AuthStep.Login, errorMessage = null)
            }
            AuthScreenEvent.ConfirmSignupVerificationClicked -> confirmSignupVerification()
            AuthScreenEvent.SignUpCodeConfirmationBackClicked -> updateState {
                copy(authStep = AuthStep.SignUpEmailVerification, errorMessage = null)
            }
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

    /** 이메일 인증이 끝난 뒤 최종 회원가입 정보를 서버로 제출한다. */
    private fun signUp() {
        val currentState = _uiState.value
        val isValid = currentState.email.isNotBlank() &&
            currentState.name.isNotBlank() &&
            currentState.password.isNotBlank() &&
            currentState.passwordConfirm.isNotBlank() &&
            currentState.verificationCode.isNotBlank()

        if (!isValid) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            val request = SignUpRequest(
                email = currentState.email,
                username = currentState.name,
                password = currentState.password,
                passwordConfirm = currentState.passwordConfirm,
                verificationCode = currentState.verificationCode,
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
                    copy(isLoading = false, statusMessage = "")
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 인증번호와 새 비밀번호를 서버에 제출해 비밀번호 재설정을 완료하고, 성공하면 로그인 단계로 되돌아간다. */
    private fun resetPassword() {
        val currentState = _uiState.value
        val isValid = currentState.passwordResetCode.isNotBlank() &&
            currentState.passwordResetNewPassword.isNotBlank() &&
            currentState.passwordResetNewPasswordConfirm.isNotBlank()

        if (!isValid) {
            updateState { copy(errorMessage = "") }
            return
        }

        if (currentState.passwordResetNewPassword != currentState.passwordResetNewPasswordConfirm) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (
                resetPasswordUseCase(
                    email = currentState.passwordResetEmail,
                    code = currentState.passwordResetCode,
                    password = currentState.passwordResetNewPassword,
                    passwordConfirm = currentState.passwordResetNewPasswordConfirm,
                )
            ) {
                is AppResult.Success -> updateState {
                    copy(
                        isLoading = false,
                        authStep = AuthStep.Login,
                        passwordResetEmail = "",
                        passwordResetCode = "",
                        passwordResetNewPassword = "",
                        passwordResetNewPasswordConfirm = "",
                        statusMessage = null,
                    )
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 회원가입 이메일 인증번호 발송을 요청하고, 성공하면 인증번호 입력 단계로 이동한다. */
    private fun sendSignupVerification() {
        val email = _uiState.value.email
        if (email.isBlank()) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (sendSignupVerificationUseCase(email)) {
                is AppResult.Success -> updateState {
                    copy(isLoading = false, authStep = AuthStep.SignUpCodeConfirmation)
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    /** 입력한 회원가입 인증번호를 서버에 확인받고, 성공하면 회원가입 입력 단계로 이동한다. */
    private fun confirmSignupVerification() {
        val currentState = _uiState.value
        if (currentState.verificationCode.isBlank()) {
            updateState { copy(errorMessage = "") }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, errorMessage = null) }
            when (confirmSignupVerificationUseCase(currentState.email, currentState.verificationCode)) {
                is AppResult.Success -> updateState {
                    copy(isLoading = false, authStep = AuthStep.SignUp)
                }
                else -> updateState { copy(isLoading = false, errorMessage = "") }
            }
        }
    }

    private fun updateState(transform: AuthUiState.() -> AuthUiState) {
        _uiState.value = _uiState.value.transform()
    }
}
