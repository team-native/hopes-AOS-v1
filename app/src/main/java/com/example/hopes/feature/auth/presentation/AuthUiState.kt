package com.example.hopes.feature.auth.presentation

data class AuthUiState(
    val authStep: AuthStep = AuthStep.Guide,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val verificationCode: String = "",
    val passwordConfirm: String = "",
    val passwordResetEmail: String = "",
    val passwordResetCode: String = "",
    val passwordResetNewPassword: String = "",
    val passwordResetNewPasswordConfirm: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
)
