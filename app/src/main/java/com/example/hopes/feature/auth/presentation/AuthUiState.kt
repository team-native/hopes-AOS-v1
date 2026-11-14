package com.example.hopes.feature.auth.presentation

data class AuthUiState(
    val authStep: AuthStep = AuthStep.Guide,
    val email: String = "",
    val password: String = "",
    val passwordResetEmail: String = "",
    val passwordResetCode: String = "",
    val passwordResetNewPassword: String = "",
    val isKeepLoginChecked: Boolean = true,
    val isLoginOffline: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
)
