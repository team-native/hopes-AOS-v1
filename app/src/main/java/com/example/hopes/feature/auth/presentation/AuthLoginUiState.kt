package com.example.hopes.feature.auth.presentation

/** 로그인 화면이 그리는 데 필요한 값만 모은 상태다. Route가 AuthUiState에서 만들어 전달한다. */
data class AuthLoginUiState(
    val emailText: String = "",
    val passwordText: String = "",
    val isKeepLoginChecked: Boolean = true,
    val isLoading: Boolean = false,
    // null이면 실패 아님, 빈 문자열이면 기본 실패 문구를 표시한다.
    val failureMessage: String? = null,
    val statusMessage: String? = null,
    val isOffline: Boolean = false,
)
