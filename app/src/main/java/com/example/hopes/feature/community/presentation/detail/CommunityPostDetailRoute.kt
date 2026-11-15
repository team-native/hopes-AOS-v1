package com.example.hopes.feature.community.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 게시글 상세다. 글은 임시 저장소에서 읽는다.
 *
 * @param onEditPost 내 글의 "수정하기"를 눌렀다. 글쓰기 화면을 수정 모드로 연다.
 */
@Composable
fun CommunityPostDetailRoute(
    postId: Long,
    onBackClick: () -> Unit,
    onEditPost: (Long) -> Unit,
    viewModel: CommunityPostDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(postId) { viewModel.initialize(postId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 내가 글을 지웠으면 목록으로 돌아간다.
    LaunchedEffect(uiState.isPostDeleted) {
        if (uiState.isPostDeleted) onBackClick()
    }

    CommunityPostDetailScreen(
        uiState = uiState,
        myCohort = viewModel.myCohort,
        onEvent = { event ->
            viewModel.onEvent(event)
            when (event) {
                CommunityPostDetailEvent.BackClicked,
                CommunityPostDetailEvent.BackToListClicked,
                -> onBackClick()

                CommunityPostDetailEvent.EditPostClicked -> onEditPost(postId)

                else -> Unit
            }
        },
    )
}
