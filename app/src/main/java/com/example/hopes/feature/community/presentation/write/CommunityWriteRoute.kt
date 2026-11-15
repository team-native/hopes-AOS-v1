package com.example.hopes.feature.community.presentation.write

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * 글쓰기·글 수정 화면이다.
 *
 * @param postId 수정할 글이다. null이면 새 글을 쓴다.
 * @param defaultCohort 새 글의 기본 게시판이다. 커뮤니티 목록에서 고른 기수를 넘긴다.
 * @param onPostCreated 새 글을 등록했다. 목록으로 돌아가 "게시글을 등록했어요"를 띄운다.
 */
@Composable
fun CommunityWriteRoute(
    postId: Long?,
    defaultCohort: Int?,
    onClose: () -> Unit,
    onPostCreated: () -> Unit,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
) {
    LaunchedEffect(postId, defaultCohort) { viewModel.initialize(postId, defaultCohort) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.result) {
        when (uiState.result) {
            CommunityWriteResult.Created -> onPostCreated()
            CommunityWriteResult.Updated, CommunityWriteResult.Closed -> onClose()
            null -> Unit
        }
    }

    CommunityWriteScreen(uiState = uiState, onEvent = viewModel::onEvent)
}
