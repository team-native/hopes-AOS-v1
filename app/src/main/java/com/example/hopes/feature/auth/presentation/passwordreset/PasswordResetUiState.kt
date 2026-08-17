package com.example.hopes.feature.auth.presentation.passwordreset

data class PasswordResetUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
)
