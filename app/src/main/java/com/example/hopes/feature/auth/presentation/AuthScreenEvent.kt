package com.example.hopes.feature.auth.presentation

sealed interface AuthScreenEvent {
    data class EmailChanged(
        val value: String,
    ) : AuthScreenEvent

    data class PasswordChanged(
        val value: String,
    ) : AuthScreenEvent

    data object LoginClicked : AuthScreenEvent

    data object SignUpRequested : AuthScreenEvent

    data object LoginRequested : AuthScreenEvent

    data object GuideRequested : AuthScreenEvent

    data class KeepLoginChanged(
        val isChecked: Boolean,
    ) : AuthScreenEvent

    data object ForgotPasswordClicked : AuthScreenEvent

    data class PasswordResetEmailChanged(
        val value: String,
    ) : AuthScreenEvent

    data class PasswordResetCodeChanged(
        val value: String,
    ) : AuthScreenEvent

    data class PasswordResetNewPasswordChanged(
        val value: String,
    ) : AuthScreenEvent

    data object PasswordResetRequestClicked : AuthScreenEvent

    data object PasswordResetSubmitClicked : AuthScreenEvent

    data object PasswordResetBackClicked : AuthScreenEvent

    /** 회원가입 흐름이 끝나 로그인 단계로 돌아온다. */
    data class SignupCompleted(
        val email: String,
        val message: String,
    ) : AuthScreenEvent
}
