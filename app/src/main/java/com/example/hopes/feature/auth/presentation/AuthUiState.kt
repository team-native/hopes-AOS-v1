package com.example.hopes.feature.auth.presentation

data class AuthUiState(
    val authStep: AuthStep = AuthStep.Guide,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val passwordResetEmail: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
)
