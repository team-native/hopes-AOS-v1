package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 회원가입 화면의 일회성 효과다. */
sealed interface SignupEffect {
    data object ShowAlreadyRegisteredDialog : SignupEffect

    data object NavigateBack : SignupEffect

    data class Completed(
        val email: String,
        val message: String,
    ) : SignupEffect
}
