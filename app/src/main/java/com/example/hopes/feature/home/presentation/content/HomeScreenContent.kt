package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesHeaderIconButton
import com.example.hopes.core.designsystem.component.HopesRetryMessage
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.home.presentation.HomePopularPost
import com.example.hopes.feature.home.presentation.HomePopularPostsState
import com.example.hopes.feature.home.presentation.HomeScreenEvent
import com.example.hopes.feature.home.presentation.HomeUiState
import com.example.hopes.feature.home.presentation.component.HomeGreetingHeader
import com.example.hopes.feature.home.presentation.component.HomeHeroCard
import com.example.hopes.feature.home.presentation.component.HomeLoadingContent
import com.example.hopes.feature.home.presentation.component.HomePopularPostCard
import com.example.hopes.feature.home.presentation.component.HomeQuestionTipCard
import com.example.hopes.feature.home.presentation.component.HomeSectionHeader
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors
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
            if (uiState.isLoading) {
                // 30-2: 인사말 대신 알림 버튼만 오른쪽에 두고 나머지는 자리 표시로 채운다.
                HopesHeaderIconButton(
                    painter = painterResource(R.drawable.ic_bell),
                    contentDescription = stringResource(R.string.home_notification),
                    onClick = { onEvent(HomeScreenEvent.NotificationClicked) },
                    modifier = Modifier.align(Alignment.End),
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HomeLoadingContent()
                return@Column
            }

            HomeGreetingHeader(
                userName = uiState.userName,
                hasUnreadNotification = uiState.hasUnreadNotification,
                onNotificationClick = { onEvent(HomeScreenEvent.NotificationClicked) },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            HomeHeroCard(onStartChatClick = { onEvent(HomeScreenEvent.StartChatClicked) })

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            HomeSectionHeader(title = stringResource(R.string.home_question_tips_title))

            // 피그마 30의 섹션 제목과 첫 카드 사이 간격이다.
            Spacer(modifier = Modifier.height(AppSpacing.Medium))

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

            if (uiState.popularPostsState == HomePopularPostsState.LoadError) {
                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HomeSectionHeader(title = stringResource(R.string.home_popular_posts_title))

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HopesRetryMessage(
                    title = stringResource(R.string.home_popular_posts_error_title),
                    description = stringResource(R.string.home_popular_posts_error_description),
                    onRetryClick = { onEvent(HomeScreenEvent.PopularPostsRetryClicked) },
                )
            } else if (uiState.popularPosts.isEmpty()) {
                // 피그마에 빈 상태 시안이 없어 인기글 줄과 같은 카드 모양으로 안내한다.
                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HomeSectionHeader(title = stringResource(R.string.home_popular_posts_title))

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HomePopularPostsEmpty()
            } else {
                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HomeSectionHeader(
                    title = stringResource(R.string.home_popular_posts_title),
                    actionText = stringResource(R.string.home_more),
                    onActionClick = { onEvent(HomeScreenEvent.PopularPostsMoreClicked) },
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact + AppSpacing.XSmall)) {
                    uiState.popularPosts.forEach { post ->
                        HomePopularPostCard(
                            post = post,
                            onClick = { onEvent(HomeScreenEvent.PopularPostClicked(post.id)) },
                            onLikeClick = { onEvent(HomeScreenEvent.PopularPostLikeClicked(post.id)) },
                        )
                    }
                }
            }
        }
    }
}

/** 인기글이 하나도 없을 때 보여주는 안내 카드다. */
@Composable
private fun HomePopularPostsEmpty() {
    val cardShape = RoundedCornerShape(AppRadius.ListCard)

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                .padding(vertical = AppSpacing.Section),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.home_popular_posts_empty),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )
    }
}

@Preview(showBackground = true, name = "30-2 홈 로딩")
@Composable
private fun HomeScreenContentLoadingPreview() {
    HopesTheme {
        HomeScreenContent(
            uiState = HomeUiState(isLoading = true),
            onEvent = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "30-3 인기글 불러오기 실패")
@Composable
private fun HomeScreenContentPopularErrorPreview() {
    HopesTheme {
        HomeScreenContent(
            uiState =
                HomeUiState(
                    userName = "서하",
                    hasUnreadNotification = true,
                    popularPostsState = HomePopularPostsState.LoadError,
                ),
            onEvent = {},
            onNavigate = {},
        )
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
