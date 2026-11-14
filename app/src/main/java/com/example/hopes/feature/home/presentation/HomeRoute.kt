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
 * 커뮤니티 서버 기능이 없어 인기글은 피그마 시안의 예시 글로 채운다.
 */
@Composable
fun HomeRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val samplePopularPosts =
        listOf(
            HomePopularPost(
                id = 1,
                cohort = null,
                title = stringResource(R.string.home_sample_post_interview),
                likeCount = 34,
            ),
            HomePopularPost(
                id = 2,
                cohort = 9,
                title = stringResource(R.string.home_sample_post_job),
                likeCount = 21,
            ),
        )

    HomeScreen(
        uiState = uiState.copy(popularPosts = samplePopularPosts),
        onEvent = { event ->
            when (event) {
                HomeScreenEvent.StartChatClicked -> onNavigate(HopesDestination.Chat())

                HomeScreenEvent.NotificationClicked -> onNotificationClick()

                HomeScreenEvent.PopularPostsMoreClicked -> onNavigate(HopesDestination.Community)

                // 게시글 상세 화면이 생기기 전까지는 커뮤니티 탭으로 보낸다.
                is HomeScreenEvent.PopularPostClicked -> onNavigate(HopesDestination.Community)
            }
        },
        onNavigate = onNavigate,
    )
}
