package com.example.hopes.feature.home.presentation

/**
 * 홈 화면 상태다.
 *
 * @param userName 인사말에 넣을 이름이다. 프로필을 아직 못 불러왔으면 null이라 이름 없는 인사말을 보여준다.
 * @param isLoading 처음 진입해 프로필을 불러오는 동안 true다. 화면 전체를 회색 자리 표시로 그린다(피그마 30-2).
 * @param popularPostsState 인기글 영역의 로딩·성공·실패 상태다.
 */
data class HomeUiState(
    val userName: String? = null,
    val isLoading: Boolean = false,
    val popularPosts: List<HomePopularPost> = emptyList(),
    val popularPostsState: HomePopularPostsState = HomePopularPostsState.Loading,
)

enum class HomePopularPostsState {
    Loading,
    Loaded,
    LoadError,
}

/**
 * 홈의 커뮤니티 인기글 한 줄이다.
 *
 * @param cohort 게시판 기수다. null이면 "전체" 게시판 글이다.
 */
data class HomePopularPost(
    val id: Long,
    val cohort: Int?,
    val title: String,
    val likeCount: Long,
    val isLiked: Boolean = false,
)
