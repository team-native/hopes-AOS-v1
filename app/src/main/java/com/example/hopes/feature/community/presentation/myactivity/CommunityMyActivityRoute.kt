package com.example.hopes.feature.community.presentation.myactivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/** 내 활동 화면이다. 마이페이지 "내 활동"과 커뮤니티 오른쪽 위 사람 버튼에서 연다. */
@Composable
fun CommunityMyActivityRoute(
    onBackClick: () -> Unit,
    onPostClick: (Long) -> Unit,
    onEditPost: (Long) -> Unit,
    onWriteClick: () -> Unit,
    viewModel: CommunityMyActivityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommunityMyActivityScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onTabSelect = viewModel::selectTab,
        onPostClick = onPostClick,
        onLikeClick = viewModel::toggleLike,
        onPostMoreClick = viewModel::openPostMore,
        onPostMoreDismiss = viewModel::closePostMore,
        onEditPostClick = { postId ->
            viewModel.closePostMore()
            onEditPost(postId)
        },
        onDeletePostClick = viewModel::requestDeletePost,
        onDeletePostConfirm = viewModel::confirmDeletePost,
        onDeletePostDismiss = viewModel::dismissDeletePost,
        onWriteClick = onWriteClick,
        onRetryClick = viewModel::retry,
    )
}
