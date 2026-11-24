package com.example.hopes.feature.community.presentation

import com.example.hopes.feature.community.model.COMMUNITY_COHORTS
import com.example.hopes.feature.community.model.CommunityPost

/**
 * @param selectedCohort 고른 기수 게시판이다. null이면 "전체"로 모든 글을 보여준다.
 * @param posts 고른 게시판의 글이다.
 */
data class CommunityUiState(
    val cohorts: List<Int> = COMMUNITY_COHORTS,
    val selectedCohort: Int? = null,
    val posts: List<CommunityPost> = emptyList(),
    val contentState: CommunityContentState = CommunityContentState.Loading,
    val nextPageState: CommunityNextPageState = CommunityNextPageState.Idle,
)

enum class CommunityNextPageState {
    Idle,
    Loading,
    Error,
}

/** 게시글 목록 자리에 보여줄 상태다(피그마 13 목록, 13-2 로딩, 19 빈 화면, 19-2 불러오기 오류). */
enum class CommunityContentState {
    Loading,
    Content,
    Empty,
    LoadError,
}
