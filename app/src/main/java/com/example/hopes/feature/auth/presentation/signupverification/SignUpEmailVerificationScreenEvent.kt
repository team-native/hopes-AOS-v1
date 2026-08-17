package com.example.hopes.feature.auth.presentation.signupverification

sealed interface SignUpEmailVerificationScreenEvent {
    data class EmailChanged(val value: String) : SignUpEmailVerificationScreenEvent
    data object RequestCodeClicked : SignUpEmailVerificationScreenEvent
    data object BackClicked : SignUpEmailVerificationScreenEvent
}
