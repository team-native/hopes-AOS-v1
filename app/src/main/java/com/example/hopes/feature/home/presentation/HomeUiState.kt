package com.example.hopes.feature.home.presentation

/**
 * 홈 화면 상태다.
 *
 * @param userName 인사말에 넣을 이름이다. 프로필을 아직 못 불러왔으면 null이라 이름 없는 인사말을 보여준다.
 * @param hasUnreadNotification 알림 버튼에 빨간 점을 표시할지 여부다.
 */
data class HomeUiState(
    val userName: String? = null,
    val hasUnreadNotification: Boolean = false,
    val popularPosts: List<HomePopularPost> = emptyList(),
)

/**
 * 홈의 커뮤니티 인기글 한 줄이다.
 *
 * @param cohort 게시판 기수다. null이면 "전체" 게시판 글이다.
 */
data class HomePopularPost(
    val id: Long,
    val cohort: Int?,
    val title: String,
    val likeCount: Int,
)
