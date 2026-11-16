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
 * 인기글은 커뮤니티 임시 저장소(`CommunityStore`)의 좋아요 많은 글이다.
 */
@Composable
fun HomeRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNotificationClick: () -> Unit,
    onPopularPostClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                HomeScreenEvent.StartChatClicked -> onNavigate(HopesDestination.Chat())

                HomeScreenEvent.NotificationClicked -> onNotificationClick()

                HomeScreenEvent.PopularPostsMoreClicked -> onNavigate(HopesDestination.Community)

                is HomeScreenEvent.PopularPostLikeClicked -> viewModel.toggleLike(event.postId)

                is HomeScreenEvent.PopularPostClicked -> onPopularPostClick(event.postId)

                // 인기글 API 연동 전이라 실패 상태가 생기지 않는다. 연동 시 여기서 다시 불러온다.
                HomeScreenEvent.PopularPostsRetryClicked -> Unit
            }
        },
        onNavigate = onNavigate,
    )
}
