package com.example.hopes.feature.auth.presentation.passwordreset

sealed interface PasswordResetScreenEvent {
    data class EmailChanged(val value: String) : PasswordResetScreenEvent
    data class CodeChanged(val value: String) : PasswordResetScreenEvent
    data class NewPasswordChanged(val value: String) : PasswordResetScreenEvent
    data class NewPasswordConfirmChanged(val value: String) : PasswordResetScreenEvent
    data object RequestCodeClicked : PasswordResetScreenEvent
    data object SubmitClicked : PasswordResetScreenEvent
    data object BackClicked : PasswordResetScreenEvent
}
