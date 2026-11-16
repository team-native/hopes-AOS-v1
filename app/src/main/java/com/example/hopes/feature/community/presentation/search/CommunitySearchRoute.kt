package com.example.hopes.feature.community.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/**
 * 게시글 검색 화면이다.
 *
 * @param cohort 검색할 게시판이다. 커뮤니티 목록에서 고른 기수를 넘긴다. null이면 모든 게시판에서 찾는다.
 */
@Composable
fun CommunitySearchRoute(
    cohort: Int?,
    onNavigate: (HopesDestination) -> Unit,
    onPostClick: (Long) -> Unit,
    onCancelClick: () -> Unit,
    viewModel: CommunitySearchViewModel = hiltViewModel(),
) {
    LaunchedEffect(cohort) { viewModel.initialize(cohort) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunitySearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::changeQuery,
        onSearch = viewModel::submitQuery,
        onRecentSearchClick = viewModel::selectRecentSearch,
        onClearRecentSearches = viewModel::clearRecentSearches,
        onPostClick = { postId ->
            // 결과를 눌러 들어간 검색어도 최근 검색어에 남긴다.
            viewModel.submitQuery()
            onPostClick(postId)
        },
        onLikeClick = viewModel::toggleLike,
        onCancelClick = onCancelClick,
        onNavigate = onNavigate,
    )
}
