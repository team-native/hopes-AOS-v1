package com.example.hopes.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopes.domain.result.AppResult
import com.example.hopes.domain.result.toDisplayMessage
import com.example.hopes.domain.usecase.LoginUseCase
import com.example.hopes.domain.usecase.RequestPasswordResetUseCase
import com.example.hopes.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthEffect {
    data object Authenticated : AuthEffect
}

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
        private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
        private val resetPasswordUseCase: ResetPasswordUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(AuthUiState())
        val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

        private val _effect = MutableSharedFlow<AuthEffect>()
        val effect: SharedFlow<AuthEffect> = _effect.asSharedFlow()

        /** 화면 이벤트로 입력값과 인증 요청을 처리한다. */
        fun onEvent(event: AuthScreenEvent) {
            when (event) {
                is AuthScreenEvent.EmailChanged -> {
                    updateEmail(event.value)
                }

                is AuthScreenEvent.PasswordChanged -> {
                    updatePassword(event.value)
                }

                AuthScreenEvent.LoginClicked -> {
                    login()
                }

                is AuthScreenEvent.SignupCompleted -> {
                    // 가입한 이메일을 로그인 입력란에 미리 채우고, 서버가 준 가입 완료 문구를 로그인 화면에 보여준다.
                    updateState {
                        copy(
                            authStep = AuthStep.Login,
                            email = event.email,
                            password = "",
                            errorMessage = null,
                            isLoginOffline = false,
                            statusMessage = event.message,
                        )
                    }
                }

                AuthScreenEvent.SignUpRequested -> {
                    updateState {
                        copy(authStep = AuthStep.SignUp, errorMessage = null)
                    }
                }

                AuthScreenEvent.LoginRequested -> {
                    updateState {
                        copy(authStep = AuthStep.Login, errorMessage = null, isLoginOffline = false)
                    }
                }

                AuthScreenEvent.GuideRequested -> {
                    updateState {
                        copy(authStep = AuthStep.Guide, errorMessage = null, isLoginOffline = false)
                    }
                }

                is AuthScreenEvent.KeepLoginChanged -> {
                    updateState { copy(isKeepLoginChecked = event.isChecked) }
                }

                AuthScreenEvent.ForgotPasswordClicked -> {
                    updateState {
                        copy(
                            authStep = AuthStep.PasswordResetRequest,
                            passwordResetEmail = passwordResetEmail.ifBlank { email },
                            errorMessage = null,
                            statusMessage = null,
                        )
                    }
                }

                is AuthScreenEvent.PasswordResetEmailChanged -> {
                    updateState {
                        copy(passwordResetEmail = event.value, errorMessage = null, statusMessage = null)
                    }
                }

                is AuthScreenEvent.PasswordResetCodeChanged -> {
                    updateState {
                        copy(passwordResetCode = event.value, errorMessage = null)
                    }
                }

                is AuthScreenEvent.PasswordResetNewPasswordChanged -> {
                    updateState {
                        copy(passwordResetNewPassword = event.value, errorMessage = null)
                    }
                }

                AuthScreenEvent.PasswordResetRequestClicked -> {
                    requestPasswordReset()
                }

                AuthScreenEvent.PasswordResetSubmitClicked -> {
                    resetPassword()
                }

                AuthScreenEvent.PasswordResetBackClicked -> {
                    updateState {
                        copy(
                            authStep = AuthStep.Login,
                            errorMessage = null,
                            statusMessage = null,
                            passwordResetCode = "",
                            passwordResetNewPassword = "",
                        )
                    }
                }
            }
        }

        /** 로그인 클릭 시 UseCase를 실행하고 결과를 UiState 또는 일회성 효과로 전달한다. */
        private fun login() {
            val currentState = _uiState.value
            if (currentState.email.isBlank() || currentState.password.isBlank()) {
                // 필수 입력이 비어 있으면 서버 요청과 로그인 실패 문구 표시를 모두 하지 않는다.
                return
            }

            viewModelScope.launch {
                updateState { copy(isLoading = true, errorMessage = null, isLoginOffline = false) }
                val result = loginUseCase(currentState.email, currentState.password)
                when (result) {
                    is AppResult.Success -> {
                        updateState { copy(isLoading = false) }
                        _effect.emit(AuthEffect.Authenticated)
                    }

                    // 네트워크 오류는 입력값 문제가 아니므로 필드 오류 대신 오프라인 배너로 안내한다.
                    AppResult.NetworkError -> {
                        updateState { copy(isLoading = false, isLoginOffline = true) }
                    }

                    else -> {
                        updateState { copy(isLoading = false, errorMessage = result.toDisplayMessage("")) }
                    }
                }
            }
        }

        /** 로그인 이메일 입력을 반영한다. 입력이 바뀌면 직전 실패 문구와 오프라인 배너를 내린다. */
        private fun updateEmail(email: String) {
            updateState {
                copy(
                    email = email,
                    errorMessage = null,
                    isLoginOffline = false,
                )
            }
        }

        /** 로그인 비밀번호 입력을 반영한다. 입력이 바뀌면 직전 실패 문구와 오프라인 배너를 내린다. */
        private fun updatePassword(password: String) {
            updateState {
                copy(
                    password = password,
                    errorMessage = null,
                    isLoginOffline = false,
                )
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
                val result = requestPasswordResetUseCase(email)
                when (result) {
                    is AppResult.Success -> {
                        updateState {
                            copy(isLoading = false, statusMessage = result.value)
                        }
                    }

                    else -> {
                        updateState { copy(isLoading = false, errorMessage = result.toDisplayMessage("")) }
                    }
                }
            }
        }

        /** 인증번호와 새 비밀번호를 서버에 제출해 비밀번호 재설정을 완료하고, 성공하면 로그인 단계로 되돌아간다. */
        private fun resetPassword() {
            val currentState = _uiState.value
            val isValid =
                currentState.passwordResetCode.isNotBlank() &&
                    currentState.passwordResetNewPassword.isNotBlank()

            if (!isValid) {
                updateState { copy(errorMessage = "") }
                return
            }

            viewModelScope.launch {
                updateState { copy(isLoading = true, errorMessage = null) }
                // 비밀번호 재설정 화면에도 별도의 비밀번호 확인 입력칸이 없어, 같은 비밀번호 값을 확인란으로도 함께 전송한다.
                val result =
                    resetPasswordUseCase(
                        email = currentState.passwordResetEmail,
                        code = currentState.passwordResetCode,
                        password = currentState.passwordResetNewPassword,
                        passwordConfirm = currentState.passwordResetNewPassword,
                    )
                when (result) {
                    is AppResult.Success -> {
                        updateState {
                            copy(
                                isLoading = false,
                                authStep = AuthStep.Login,
                                passwordResetEmail = "",
                                passwordResetCode = "",
                                passwordResetNewPassword = "",
                                statusMessage = null,
                            )
                        }
                    }

                    else -> {
                        updateState { copy(isLoading = false, errorMessage = result.toDisplayMessage("")) }
                    }
                }
            }
        }

        private fun updateState(transform: AuthUiState.() -> AuthUiState) {
            _uiState.value = _uiState.value.transform()
        }
    }
