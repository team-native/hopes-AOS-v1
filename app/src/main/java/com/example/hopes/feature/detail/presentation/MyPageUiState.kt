package com.example.hopes.feature.detail.presentation

/** 서버 프로필과 마이페이지 요청 상태를 표현한다. */
data class MyPageUiState(
    val profileName: String = "",
    val profileIntroduction: String = "",
    val email: String = "",
    val major: String? = null,
    val isProfileLoading: Boolean = true,
    val isProfileLoadFailed: Boolean = false,
    val isProfileSaving: Boolean = false,
    val isProfileSaved: Boolean = false,
    val isProfileSaveFailed: Boolean = false,
)
