package com.example.hopes.feature.community.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination
import com.teamnative.hopes.R

/**
 * 커뮤니티 탭의 게시글을 서버에서 불러온다.
 *
 * @param onWriteClick 글쓰기 화면을 연다. 지금 고른 기수 게시판을 넘겨 기본 게시판으로 쓴다.
 * @param onSearchClick 검색 화면을 연다. 지금 고른 기수 게시판 안에서 찾는다.
 * @param isPostCreatedToastVisible 글을 등록하고 돌아왔으면 true다. "게시글을 등록했어요"를 띄운다.
 */
@Composable
fun CommunityRoute(
    onNavigate: (HopesDestination) -> Unit,
    onPostClick: (Long) -> Unit,
    onSearchClick: (Int?) -> Unit,
    onMyActivityClick: () -> Unit,
    onWriteClick: (Int?) -> Unit,
    isPostCreatedToastVisible: Boolean,
    onToastDismiss: () -> Unit,
    viewModel: CommunityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityScreen(
        uiState = uiState,
        onCohortSelect = viewModel::selectCohort,
        onPostClick = onPostClick,
        onLikeClick = viewModel::toggleLike,
        onSearchClick = { onSearchClick(uiState.selectedCohort) },
        onMyActivityClick = onMyActivityClick,
        onWriteClick = { onWriteClick(uiState.selectedCohort) },
        onRetryClick = viewModel::retry,
        onLoadMore = viewModel::loadNextPage,
        onRetryNextPage = viewModel::retryNextPage,
        onNavigate = onNavigate,
        toastMessage = if (isPostCreatedToastVisible) stringResource(R.string.community_post_created) else null,
        onToastDismiss = onToastDismiss,
    )
}
