package com.example.hopes.feature.auth.presentation.signupverification

data class SignUpEmailVerificationUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
