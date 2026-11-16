package com.example.hopes.feature.community.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesSearchField
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.feature.community.model.CommunityPost
import com.example.hopes.feature.community.presentation.component.CommunityPostCard
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDateTime

/**
 * 게시글 검색 화면이다(피그마 16, 16-2, 16-3). 검색어가 없으면 최근 검색어를, 있으면 결과를 보여준다.
 * 화면을 열면 바로 검색창에 커서를 둔다.
 */
@Composable
fun CommunitySearchScreen(
    uiState: CommunitySearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit = {},
    onCancelClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    shouldFocusSearchField: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val now = remember(uiState.results) { LocalDateTime.now() }

    // 처음 열었을 때만 커서를 둔다. 결과를 보고 돌아왔을 때 키보드가 다시 올라오지 않게 한다.
    var hasRequestedFocus by rememberSaveable { mutableStateOf(!shouldFocusSearchField) }
    LaunchedEffect(Unit) {
        if (!hasRequestedFocus) {
            focusRequester.requestFocus()
            hasRequestedFocus = true
        }
    }

    FigmaAppFrame(
        selectedDestination = HopesDestination.Community,
        onNavigate = onNavigate,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(AppSpacing.SystemBarToContent))

            Row(
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HopesSearchField(
                    value = uiState.query,
                    onValueChange = onQueryChange,
                    placeholder = stringResource(R.string.community_search_placeholder),
                    onSearch = onSearch,
                    focusRequester = focusRequester,
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Item))

                Text(
                    text = stringResource(R.string.cancel),
                    modifier = Modifier.clickable(role = Role.Button, onClick = onCancelClick),
                    color = MaterialTheme.colorScheme.primary,
                    style = HopesTextStyles.LabelStrong,
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    uiState.query.isBlank() -> {
                        CommunityRecentSearches(
                            recentSearches = uiState.recentSearches,
                            onRecentSearchClick = onRecentSearchClick,
                            onClearClick = onClearRecentSearches,
                            modifier = Modifier.align(Alignment.TopStart),
                        )
                    }

                    uiState.results.isEmpty() -> {
                        HopesStatusMessage(
                            icon = Icons.Outlined.Search,
                            title = stringResource(R.string.community_search_empty_title),
                            description = stringResource(R.string.community_search_empty_description),
                        )
                    }

                    else -> {
                        CommunitySearchResults(
                            results = uiState.results,
                            now = now,
                            onPostClick = onPostClick,
                            onLikeClick = onLikeClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommunitySearchResults(
    results: List<CommunityPost>,
    now: LocalDateTime,
    onPostClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(
                start = AppSpacing.ScreenHorizontal,
                end = AppSpacing.ScreenHorizontal,
                top = AppSpacing.Medium,
                bottom = AppSpacing.Section,
            ),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
    ) {
        item {
            Text(
                text = stringResource(R.string.community_search_result_count, results.size),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.LabelStrong,
            )
        }

        items(results, key = { it.id }) { post ->
            CommunityPostCard(
                post = post,
                now = now,
                onClick = { onPostClick(post.id) },
                onLikeClick = { onLikeClick(post.id) },
            )
        }
    }
}

/** "최근 검색어 · 전체 삭제"와 검색어 칩이다(피그마 16-2). 최근 검색어가 없으면 아무것도 그리지 않는다. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CommunityRecentSearches(
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (recentSearches.isEmpty()) return

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.ScreenHorizontal, vertical = AppSpacing.Medium),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.community_recent_searches),
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.LabelStrong,
            )

            Text(
                text = stringResource(R.string.community_recent_searches_clear),
                modifier = Modifier.clickable(role = Role.Button, onClick = onClearClick),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
        ) {
            recentSearches.forEach { keyword ->
                Box(
                    modifier =
                        Modifier
                            .height(AppSize.Chip)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(AppBorderWidth.Default, MaterialTheme.colorScheme.outline, CircleShape)
                            .clickable(role = Role.Button, onClick = { onRecentSearchClick(keyword) })
                            .padding(horizontal = AppSpacing.Item + AppSpacing.XSmall),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = keyword,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = HopesTextStyles.Label,
                    )
                }
            }
        }
    }
}

@Composable
private fun CommunitySearchPreview(uiState: CommunitySearchUiState) {
    HopesTheme {
        CommunitySearchScreen(
            uiState = uiState,
            onQueryChange = {},
            onSearch = {},
            onRecentSearchClick = {},
            onClearRecentSearches = {},
            onPostClick = {},
            onCancelClick = {},
            onNavigate = {},
            shouldFocusSearchField = false,
        )
    }
}

@Preview(showBackground = true, name = "16 게시글 검색")
@Composable
private fun CommunitySearchResultsPreview() {
    val now = LocalDateTime.now()
    CommunitySearchPreview(
        CommunitySearchUiState(
            query = "기숙사",
            results =
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
                ),
        ),
    )
}

@Preview(showBackground = true, name = "16-2 최근 검색어")
@Composable
private fun CommunitySearchRecentPreview() {
    CommunitySearchPreview(CommunitySearchUiState(recentSearches = listOf("기숙사", "동아리 면접", "포트폴리오")))
}

@Preview(showBackground = true, name = "16-3 검색 결과 없음")
@Composable
private fun CommunitySearchEmptyPreview() {
    CommunitySearchPreview(CommunitySearchUiState(query = "수학여행 일정"))
}
