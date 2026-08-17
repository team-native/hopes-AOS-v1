package com.example.hopes.feature.auth.presentation.signupverification

sealed interface SignUpCodeConfirmationScreenEvent {
    data class CodeChanged(val value: String) : SignUpCodeConfirmationScreenEvent
    data object ConfirmClicked : SignUpCodeConfirmationScreenEvent
    data object BackClicked : SignUpCodeConfirmationScreenEvent
}
