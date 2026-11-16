package com.example.hopes.feature.community.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.presentation.component.CommunityCohortFilterRow
import com.example.hopes.feature.community.presentation.component.CommunityHeader
import com.example.hopes.feature.community.presentation.component.CommunityPostCard
import com.example.hopes.feature.community.presentation.component.CommunityPostListSkeleton
import com.example.hopes.feature.community.presentation.component.CommunityWriteButton
import com.example.hopes.feature.community.presentation.component.communityBoardName
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 커뮤니티 탭이다(피그마 13, 13-2, 13-3, 19, 19-2). 제목·기수 필터 아래에 게시글 목록 또는 상태 안내를 그린다.
 *
 * @param toastMessage 글을 등록하고 돌아왔을 때 띄울 안내다(피그마 15-8). null이면 띄우지 않는다.
 */
@Composable
fun CommunityScreen(
    uiState: CommunityUiState,
    onCohortSelect: (Int?) -> Unit,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit = {},
    onSearchClick: () -> Unit,
    onMyActivityClick: () -> Unit,
    onWriteClick: () -> Unit,
    onRetryClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    toastMessage: String? = null,
    onToastDismiss: () -> Unit = {},
) {
    val now = remember(uiState.posts) { LocalDateTime.now() }
    val listState = rememberLazyListState()
    val topPostId = uiState.posts.firstOrNull()?.id

    // 새 글이 맨 위에 생기면 목록을 맨 위로 올린다. 그대로 두면 보던 글 기준으로 위치가 유지돼 새 글이 화면 밖에 남는다.
    LaunchedEffect(topPostId) {
        if (topPostId != null) listState.scrollToItem(0)
    }

    FigmaAppFrame(
        selectedDestination = HopesDestination.Community,
        onNavigate = onNavigate,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(AppSpacing.SystemBarToContent))

                CommunityHeader(
                    onSearchClick = onSearchClick,
                    onMyActivityClick = onMyActivityClick,
                    modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                CommunityCohortFilterRow(
                    cohorts = uiState.cohorts,
                    selectedCohort = uiState.selectedCohort,
                    onCohortSelect = onCohortSelect,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = AppSpacing.ScreenHorizontal),
                )

                if (uiState.selectedCohort != null) {
                    Text(
                        text =
                            stringResource(
                                R.string.community_board_caption,
                                communityBoardName(uiState.selectedCohort),
                            ),
                        modifier =
                            Modifier.padding(
                                start = AppSpacing.ScreenHorizontal,
                                end = AppSpacing.ScreenHorizontal,
                                top = AppSpacing.Item,
                            ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = HopesTextStyles.Caption,
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CommunityContent(
                        uiState = uiState,
                        listState = listState,
                        now = now,
                        onPostClick = onPostClick,
                        onLikeClick = onLikeClick,
                        onWriteClick = onWriteClick,
                        onRetryClick = onRetryClick,
                    )
                }
            }

            if (uiState.contentState == CommunityContentState.Content) {
                CommunityWriteButton(
                    onClick = onWriteClick,
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            // 피그마 FAB: 오른쪽 20, 하단 탭바 위 26.
                            .padding(end = AppSpacing.ScreenHorizontal, bottom = FAB_BOTTOM_MARGIN),
                )
            }

            if (toastMessage != null) {
                HopesToast(
                    message = toastMessage,
                    onDismiss = onToastDismiss,
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppSpacing.Section),
                )
            }
        }
    }
}

@Composable
private fun CommunityContent(
    uiState: CommunityUiState,
    listState: LazyListState,
    now: LocalDateTime,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit = {},
    onWriteClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    when (uiState.contentState) {
        CommunityContentState.Loading -> {
            CommunityPostListSkeleton(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppSpacing.ScreenHorizontal, vertical = AppSpacing.Item),
            )
        }

        CommunityContentState.Content -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                contentPadding =
                    PaddingValues(
                        start = AppSpacing.ScreenHorizontal,
                        end = AppSpacing.ScreenHorizontal,
                        top = AppSpacing.Medium,
                        // 마지막 글이 글쓰기 버튼에 가리지 않게 버튼 높이만큼 더 띄운다.
                        bottom = AppSpacing.ScreenHorizontal + AppSize.Field + AppSpacing.Section,
                    ),
                verticalArrangement = Arrangement.spacedBy(POST_GAP),
            ) {
                items(uiState.posts, key = { it.id }) { post ->
                    CommunityPostCard(
                        post = post,
                        now = now,
                        onClick = { onPostClick(post.id) },
                        onLikeClick = { onLikeClick(post.id) },
                    )
                }
            }
        }

        CommunityContentState.Empty -> {
            HopesStatusMessage(
                iconPainter = painterResource(R.drawable.ic_chat_large),
                title = stringResource(R.string.community_empty_title),
                description = stringResource(R.string.community_empty_description),
                actionText = stringResource(R.string.community_write),
                onActionClick = onWriteClick,
            )
        }

        CommunityContentState.LoadError -> {
            HopesStatusMessage(
                icon = Icons.Outlined.ErrorOutline,
                title = stringResource(R.string.community_load_error_title),
                description = stringResource(R.string.community_load_error_description),
                actionText = stringResource(R.string.community_retry),
                onActionClick = onRetryClick,
            )
        }
    }
}

private val POST_GAP = 10.dp
private val FAB_BOTTOM_MARGIN = 26.dp

@Composable
private fun CommunityScreenPreview(uiState: CommunityUiState) {
    HopesTheme {
        CommunityScreen(
            uiState = uiState,
            onCohortSelect = {},
            onPostClick = {},
            onSearchClick = {},
            onMyActivityClick = {},
            onWriteClick = {},
            onRetryClick = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "13 커뮤니티 목록")
@Composable
private fun CommunityScreenContentPreview() {
    val now = LocalDateTime.now()
    CommunityScreenPreview(
        CommunityUiState(
            posts =
                listOf(
                    CommunityPost(
                        id = 1,
                        cohort = 10,
                        authorCohort = 10,
                        title = "기숙사 와이파이 다들 잘 돼?",
                        content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서… 다른 층은 어떤지 궁금해.",
                        createdAt = now.minusMinutes(10),
                        likeCount = 12,
                        commentCount = 5,
                    ),
                    CommunityPost(
                        id = 2,
                        cohort = null,
                        authorCohort = 10,
                        title = "전공 동아리 면접 후기 공유",
                        content = "어제 보고 왔는데 생각보다 분위기 편했어. 질문은 주로 지원 동기 위주였음.",
                        createdAt = now.minusHours(1),
                        likeCount = 34,
                        commentCount = 8,
                    ),
                ),
            contentState = CommunityContentState.Content,
        ),
    )
}

@Preview(showBackground = true, name = "13-2 커뮤니티 로딩")
@Composable
private fun CommunityScreenLoadingPreview() {
    CommunityScreenPreview(CommunityUiState(contentState = CommunityContentState.Loading))
}

@Preview(showBackground = true, name = "19 커뮤니티 빈 화면")
@Composable
private fun CommunityScreenEmptyPreview() {
    CommunityScreenPreview(CommunityUiState(contentState = CommunityContentState.Empty))
}

@Preview(showBackground = true, name = "19-2 커뮤니티 불러오기 오류")
@Composable
private fun CommunityScreenLoadErrorPreview() {
    CommunityScreenPreview(CommunityUiState(contentState = CommunityContentState.LoadError))
}
