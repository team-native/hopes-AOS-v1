package com.example.hopes.feature.detail.presentation

/** 서버 프로필 조회·저장 상태와 마이페이지 입력값을 표현한다. */
data class MyPageUiState(
    val profileName: String = "",
    val profileIntroduction: String = "",
    val isProfileLoading: Boolean = true,
    val isProfileLoadFailed: Boolean = false,
    val isProfileSaving: Boolean = false,
    val isProfileSaved: Boolean = false,
    val isProfileSaveFailed: Boolean = false,
)
