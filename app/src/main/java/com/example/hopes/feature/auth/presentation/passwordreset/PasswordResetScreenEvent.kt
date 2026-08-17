package com.example.hopes.feature.auth.presentation.passwordreset

sealed interface PasswordResetScreenEvent {
    data class EmailChanged(val value: String) : PasswordResetScreenEvent
    data object RequestCodeClicked : PasswordResetScreenEvent
    data object BackClicked : PasswordResetScreenEvent
}
