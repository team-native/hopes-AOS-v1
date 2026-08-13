package com.example.hopes.feature.auth.presentation

import com.example.hopes.domain.result.AppError

/** 로그인 화면이 표시할 입력값과 서버 요청 상태다. */
data class AuthUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val isAuthenticated: Boolean = false,
)
