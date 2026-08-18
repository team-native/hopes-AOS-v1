package com.example.hopes.feature.auth.presentation

sealed interface AuthScreenEvent {
    data class EmailChanged(val value: String) : AuthScreenEvent
    data class PasswordChanged(val value: String) : AuthScreenEvent
    data class NameChanged(val value: String) : AuthScreenEvent
    data class DepartmentChanged(val value: String) : AuthScreenEvent
    data class GenerationChanged(val value: String) : AuthScreenEvent
    data class VerificationCodeChanged(val value: String) : AuthScreenEvent
    data object LoginClicked : AuthScreenEvent
    data object SignUpClicked : AuthScreenEvent
    data object SignUpRequested : AuthScreenEvent
    data object LoginRequested : AuthScreenEvent
    data object LoginDismissed : AuthScreenEvent
    data object ForgotPasswordClicked : AuthScreenEvent
    data class PasswordResetEmailChanged(val value: String) : AuthScreenEvent
    data class PasswordResetCodeChanged(val value: String) : AuthScreenEvent
    data class PasswordResetNewPasswordChanged(val value: String) : AuthScreenEvent
    data object PasswordResetRequestClicked : AuthScreenEvent
    data object PasswordResetSubmitClicked : AuthScreenEvent
    data object PasswordResetBackClicked : AuthScreenEvent
    data object SendSignupVerificationClicked : AuthScreenEvent
}
