package com.example.hopes.feature.detail.presentation

/** 마이페이지에 보여줄 서버 프로필 요약과 활동 통계다. */
data class MyPageUiState(
    val profileName: String = "",
    val email: String = "",
    val major: String? = null,
    val isProfileLoading: Boolean = true,
    val isProfileLoadFailed: Boolean = false,
    val activityStats: MyPageActivityStats? = MyPageActivityStats(),
)

/**
 * 마이페이지 통계 카드의 작성글·댓글·대화 수다.
 *
 * @param chatCount 서버에서 센 내 대화 수다. 아직 세지 못했거나 실패하면 null이고 "-"로 보여준다.
 */
data class MyPageActivityStats(
    val postCount: Int = 0,
    val commentCount: Int = 0,
    val chatCount: Int? = null,
)
