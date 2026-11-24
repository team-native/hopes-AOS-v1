package com.example.hopes.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination
import com.teamnative.hopes.R

/**
 * 홈 화면 이벤트를 앱 탐색 동작에 연결한다.
 * 인기글은 커뮤니티 서버 게시글에서 조회한다.
 */
@Composable
fun HomeRoute(
    onNavigate: (HopesDestination) -> Unit,
    onPopularPostClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                HomeScreenEvent.StartChatClicked -> onNavigate(HopesDestination.Chat())

                HomeScreenEvent.PopularPostsMoreClicked -> onNavigate(HopesDestination.Community)

                is HomeScreenEvent.PopularPostLikeClicked -> viewModel.toggleLike(event.postId)

                is HomeScreenEvent.PopularPostClicked -> onPopularPostClick(event.postId)

                HomeScreenEvent.PopularPostsRetryClicked -> viewModel.retryPopularPosts()
            }
        },
        onNavigate = onNavigate,
    )
}
