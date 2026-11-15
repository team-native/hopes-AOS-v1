package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesRetryMessage
import com.example.hopes.core.designsystem.component.HopesSkeletonLine
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.feature.history.presentation.ChatSummaryUiModel
import com.example.hopes.feature.history.presentation.HistoryContentState
import com.example.hopes.feature.history.presentation.HistoryScreenEvent
import com.example.hopes.feature.history.presentation.HistorySection
import com.example.hopes.feature.history.presentation.HistoryUiState
import com.example.hopes.feature.history.presentation.component.HistoryChatGroup
import com.example.hopes.feature.history.presentation.component.HistorySearchField
import com.example.hopes.feature.history.presentation.historyDateLabelOf
import com.example.hopes.feature.history.presentation.historySectionOf
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R
import java.time.LocalDate

/**
 * 피그마 24 지난 대화 화면이다. 제목·검색창 아래에 대화를 "지난 7일 / 이전"으로 묶어 보여주고,
 * 검색 중에는 "검색 결과 N개" 한 묶음으로 보여준다. 하단에 새 대화 시작 버튼을 고정한다.
 *
 * @param today 날짜 묶음 기준일이다. 테스트와 Preview에서 고정하려고 받는다.
 */
@Composable
fun HistoryScreenContent(
    uiState: HistoryUiState,
    onEvent: (HistoryScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    today: LocalDate = LocalDate.now(),
) {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
        isBottomNavigationVisible = !isImeVisible,
        fixedBottomContent = {
            if (!isImeVisible) {
                HopesPrimaryButton(
                    text = stringResource(R.string.history_new_chat),
                    onClick = { onEvent(HistoryScreenEvent.NewChatClicked) },
                    modifier =
                        Modifier.padding(
                            start = AppSpacing.ScreenHorizontal,
                            end = AppSpacing.ScreenHorizontal,
                            bottom = AppSpacing.Item,
                        ),
                )
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier.padding(
                        start = AppSpacing.ScreenHorizontal,
                        top = AppSpacing.SystemBarToContent,
                        end = AppSpacing.ScreenHorizontal,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.history_title),
                    modifier = Modifier.semantics { heading() },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = HopesTextStyles.PageTitle,
                )

                Text(
                    text = stringResource(R.string.history_subtitle),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Subtitle,
                )

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                HistorySearchField(
                    value = uiState.searchQuery,
                    onValueChange = { onEvent(HistoryScreenEvent.SearchQueryChanged(it)) },
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            ) {
                HistoryBody(uiState = uiState, onEvent = onEvent, today = today)
            }
        }
    }

    uiState.pendingDeleteChatId?.let {
        HopesAlertDialog(
            title = stringResource(R.string.history_delete_confirm_title),
            message = stringResource(R.string.history_delete_confirm_message),
            dismissText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.history_delete),
            onDismiss = { onEvent(HistoryScreenEvent.DeleteChatDismissed) },
            onConfirm = { onEvent(HistoryScreenEvent.DeleteChatConfirmed) },
            isConfirmDestructive = true,
        )
    }
}

@Composable
private fun HistoryBody(
    uiState: HistoryUiState,
    onEvent: (HistoryScreenEvent) -> Unit,
    today: LocalDate,
) {
    val isSearching = uiState.searchQuery.isNotBlank()

    when (uiState.contentState) {
        HistoryContentState.Loading -> {
            HistoryLoading()
        }

        HistoryContentState.Error -> {
            HopesRetryMessage(
                title = stringResource(R.string.history_error_title),
                description = stringResource(R.string.history_error_description),
                onRetryClick = { onEvent(HistoryScreenEvent.RetryClicked) },
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal, vertical = AppSpacing.Section),
            )
        }

        HistoryContentState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isSearching) {
                    HopesStatusMessage(
                        icon = Icons.Outlined.Search,
                        title = stringResource(R.string.history_no_result_title),
                        description = stringResource(R.string.history_no_result_description),
                    )
                } else {
                    HopesStatusMessage(
                        icon = Icons.AutoMirrored.Outlined.Chat,
                        title = stringResource(R.string.history_empty_title),
                        description = stringResource(R.string.history_empty_description),
                        actionText = stringResource(R.string.start_chat),
                        onActionClick = { onEvent(HistoryScreenEvent.NewChatClicked) },
                    )
                }
            }
        }

        HistoryContentState.Content -> {
            HistoryChatList(
                uiState = uiState,
                isSearching = isSearching,
                onEvent = onEvent,
                today = today,
            )
        }
    }
}

@Composable
private fun HistoryChatList(
    uiState: HistoryUiState,
    isSearching: Boolean,
    onEvent: (HistoryScreenEvent) -> Unit,
    today: LocalDate,
) {
    val listState = rememberLazyListState()
    val isAtEnd by remember { derivedStateOf { !listState.canScrollForward } }
    val sections =
        remember(uiState.chats, isSearching, today) {
            if (isSearching) {
                emptyMap()
            } else {
                uiState.chats.groupBy { historySectionOf(it.date, today) }
            }
        }

    // 목록 끝에 닿으면 다음 페이지를 불러온다.
    LaunchedEffect(isAtEnd, uiState.hasNextPage, uiState.isLoadingNextPage, uiState.chats.size) {
        if (isAtEnd && uiState.hasNextPage && !uiState.isLoadingNextPage && !uiState.isNextPageError) {
            onEvent(HistoryScreenEvent.LoadNextPageClicked)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding =
            PaddingValues(
                start = AppSpacing.ScreenHorizontal,
                top = AppSpacing.Section,
                end = AppSpacing.ScreenHorizontal,
                bottom = AppSpacing.Section,
            ),
    ) {
        if (isSearching) {
            item(key = "search") {
                HistoryGroup(
                    label = stringResource(R.string.history_search_result_count, uiState.chats.size),
                    chats = uiState.chats,
                    highlight = uiState.searchQuery,
                    today = today,
                    onEvent = onEvent,
                )
            }
        } else {
            HistorySection.entries.forEach { section ->
                val chats = sections[section].orEmpty()
                if (chats.isNotEmpty()) {
                    item(key = section.name) {
                        HistoryGroup(
                            label =
                                stringResource(
                                    when (section) {
                                        HistorySection.LastSevenDays -> R.string.history_recent
                                        HistorySection.Earlier -> R.string.history_previous
                                    },
                                ),
                            chats = chats,
                            highlight = "",
                            today = today,
                            onEvent = onEvent,
                        )
                    }
                }
            }
        }

        if (uiState.isLoadingNextPage) {
            item(key = "loading") {
                Box(modifier = Modifier.fillMaxWidth().padding(AppSpacing.Item), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (uiState.isNextPageError) {
            item(key = "nextPageError") {
                HopesRetryMessage(
                    title = stringResource(R.string.history_error_title),
                    description = stringResource(R.string.history_load_next_page_error),
                    onRetryClick = { onEvent(HistoryScreenEvent.LoadNextPageRetryClicked) },
                )
            }
        }
    }
}

@Composable
private fun HistoryGroup(
    label: String,
    chats: List<ChatSummaryUiModel>,
    highlight: String,
    today: LocalDate,
    onEvent: (HistoryScreenEvent) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = AppSpacing.Section)) {
        Text(
            text = label,
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.CaptionStrong,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        HistoryChatGroup(
            chats = chats,
            highlight = highlight,
            dateLabelOf = { chat -> historyDateLabelOf(chat.date, today) },
            onChatClick = { onEvent(HistoryScreenEvent.ChatClicked(it)) },
            onDeleteClick = { onEvent(HistoryScreenEvent.DeleteChatClicked(it)) },
        )
    }
}

@Composable
private fun HistoryLoading() {
    Column(
        modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal, vertical = AppSpacing.Section),
    ) {
        repeat(LOADING_ROW_COUNT) {
            HopesSkeletonLine(modifier = Modifier.fillMaxWidth(LOADING_LINE_FRACTION))
            Spacer(modifier = Modifier.height(AppSpacing.Section))
        }
    }
}

private const val LOADING_ROW_COUNT = 5
private const val LOADING_LINE_FRACTION = 0.8f

private val previewToday = LocalDate.of(2026, 11, 14)
private val previewChats =
    listOf(
        ChatSummaryUiModel(id = 1, title = "기숙사 하루 일과가 어떻게 돼?", date = previewToday.minusDays(1)),
        ChatSummaryUiModel(id = 2, title = "전공 선택은 어떻게 해야 하는 게 좋아?", date = previewToday.minusDays(2)),
        ChatSummaryUiModel(id = 3, title = "동아리는 언제 정해?", date = previewToday.minusDays(5)),
        ChatSummaryUiModel(id = 4, title = "입학하려면 뭘 준비해야 해?", date = previewToday.minusDays(20)),
        ChatSummaryUiModel(id = 5, title = "후배한테 해주고 싶은 조언 있어?", date = previewToday.minusDays(40)),
    )

@Preview(showBackground = true, name = "24 지난 대화")
@Composable
private fun HistoryScreenContentPreview() {
    HopesTheme {
        HistoryScreenContent(
            uiState = HistoryUiState(chats = previewChats, contentState = HistoryContentState.Content),
            onEvent = {},
            onNavigate = {},
            today = previewToday,
        )
    }
}

@Preview(showBackground = true, name = "24-2 검색")
@Composable
private fun HistoryScreenContentSearchPreview() {
    HopesTheme {
        HistoryScreenContent(
            uiState =
                HistoryUiState(
                    searchQuery = "기숙사",
                    chats = previewChats.take(1),
                    contentState = HistoryContentState.Content,
                ),
            onEvent = {},
            onNavigate = {},
            today = previewToday,
        )
    }
}

@Preview(showBackground = true, name = "24-3 빈 상태")
@Composable
private fun HistoryScreenContentEmptyPreview() {
    HopesTheme {
        HistoryScreenContent(
            uiState = HistoryUiState(contentState = HistoryContentState.Empty),
            onEvent = {},
            onNavigate = {},
            today = previewToday,
        )
    }
}

@Preview(showBackground = true, name = "24-4 검색 결과 없음")
@Composable
private fun HistoryScreenContentNoResultPreview() {
    HopesTheme {
        HistoryScreenContent(
            uiState = HistoryUiState(searchQuery = "수학여행", contentState = HistoryContentState.Empty),
            onEvent = {},
            onNavigate = {},
            today = previewToday,
        )
    }
}

@Preview(showBackground = true, name = "24-6 대화 삭제 확인")
@Composable
private fun HistoryScreenContentDeletePreview() {
    HopesTheme {
        HistoryScreenContent(
            uiState =
                HistoryUiState(
                    chats = previewChats,
                    contentState = HistoryContentState.Content,
                    pendingDeleteChatId = 1,
                ),
            onEvent = {},
            onNavigate = {},
            today = previewToday,
        )
    }
}
