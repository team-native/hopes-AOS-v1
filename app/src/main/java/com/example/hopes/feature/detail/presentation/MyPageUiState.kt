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
    val activityStats: MyPageActivityStats? = null,
)

/** 마이페이지 통계 카드의 작성글·댓글·대화 수다. */
data class MyPageActivityStats(
    val postCount: Int,
    val commentCount: Int,
    val chatCount: Int,
)
