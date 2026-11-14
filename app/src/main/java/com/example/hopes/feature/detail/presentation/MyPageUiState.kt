package com.example.hopes.feature.detail.presentation

/** 마이페이지에 보여줄 서버 프로필 요약과 활동 통계다. */
data class MyPageUiState(
    val profileName: String = "",
    val email: String = "",
    val major: String? = null,
    val isProfileLoading: Boolean = true,
    val isProfileLoadFailed: Boolean = false,
    val activityStats: MyPageActivityStats? = null,
)

/** 마이페이지 통계 카드의 작성글·댓글·대화 수다. */
data class MyPageActivityStats(
    val postCount: Int,
    val commentCount: Int,
    val chatCount: Int,
)
