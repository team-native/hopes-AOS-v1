package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.home.presentation.HomePopularPost
import com.example.hopes.feature.home.presentation.HomeScreenEvent
import com.example.hopes.feature.home.presentation.HomeUiState
import com.example.hopes.feature.home.presentation.component.HomeGreetingHeader
import com.example.hopes.feature.home.presentation.component.HomeHeroCard
import com.example.hopes.feature.home.presentation.component.HomePopularPostCard
import com.example.hopes.feature.home.presentation.component.HomeQuestionTipCard
import com.example.hopes.feature.home.presentation.component.HomeSectionHeader
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 30 홈 화면이다. 인사말, 채팅 시작 카드, 질문 팁, 커뮤니티 인기글을 위에서부터 쌓는다. */
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Home,
        onNavigate = onNavigate,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppSpacing.ScreenHorizontal,
                        top = AppSpacing.SystemBarToContent,
                        end = AppSpacing.ScreenHorizontal,
                        bottom = AppSpacing.Section,
                    ),
        ) {
            HomeGreetingHeader(
                userName = uiState.userName,
                hasUnreadNotification = uiState.hasUnreadNotification,
                onNotificationClick = { onEvent(HomeScreenEvent.NotificationClicked) },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            HomeHeroCard(onStartChatClick = { onEvent(HomeScreenEvent.StartChatClicked) })

            Spacer(modifier = Modifier.height(AppSpacing.Section + AppSpacing.Compact))

            HomeSectionHeader(title = stringResource(R.string.home_question_tips_title))

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact + AppSpacing.XSmall)) {
                HomeQuestionTipCard(
                    index = 1,
                    topText = stringResource(R.string.home_tip_major_top),
                    bottomText = stringResource(R.string.home_tip_major_bottom),
                )

                HomeQuestionTipCard(
                    index = 2,
                    topText = stringResource(R.string.home_tip_specific_top),
                    bottomText = stringResource(R.string.home_tip_specific_bottom),
                )
            }

            if (uiState.popularPosts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(AppSpacing.Section))

                HomeSectionHeader(
                    title = stringResource(R.string.home_popular_posts_title),
                    actionText = stringResource(R.string.home_more),
                    onActionClick = { onEvent(HomeScreenEvent.PopularPostsMoreClicked) },
                )

                Spacer(modifier = Modifier.height(AppSpacing.Compact))

                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact + AppSpacing.XSmall)) {
                    uiState.popularPosts.forEach { post ->
                        HomePopularPostCard(
                            post = post,
                            onClick = { onEvent(HomeScreenEvent.PopularPostClicked(post.id)) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "30 홈")
@Composable
private fun HomeScreenContentPreview() {
    HopesTheme {
        HomeScreenContent(
            uiState =
                HomeUiState(
                    userName = "서하",
                    hasUnreadNotification = true,
                    popularPosts =
                        listOf(
                            HomePopularPost(id = 1, cohort = null, title = "전공 동아리 면접 후기 공유", likeCount = 34),
                            HomePopularPost(id = 2, cohort = 9, title = "취업 준비 언제부터 시작했어?", likeCount = 21),
                        ),
                ),
            onEvent = {},
            onNavigate = {},
        )
    }
}
