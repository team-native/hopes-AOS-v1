package com.example.hopes.feature.auth.presentation.signupverification

data class SignUpCodeConfirmationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
