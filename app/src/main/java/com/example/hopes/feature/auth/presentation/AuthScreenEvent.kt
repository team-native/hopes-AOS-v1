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
    data object SendVerificationCodeClicked : AuthScreenEvent
    data object SignUpRequested : AuthScreenEvent
    data object LoginRequested : AuthScreenEvent
    data object LoginDismissed : AuthScreenEvent
}
