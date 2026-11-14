package com.example.hopes.feature.auth.presentation

/** 로그인 화면에서 Route로 전달되는 사용자 이벤트다. */
sealed interface AuthLoginScreenEvent {
    data class EmailChanged(
        val value: String,
    ) : AuthLoginScreenEvent

    data class PasswordChanged(
        val value: String,
    ) : AuthLoginScreenEvent

    data class KeepLoginChanged(
        val isChecked: Boolean,
    ) : AuthLoginScreenEvent

    data object LoginClicked : AuthLoginScreenEvent

    data object FindPasswordClicked : AuthLoginScreenEvent

    data object SignUpClicked : AuthLoginScreenEvent
}
