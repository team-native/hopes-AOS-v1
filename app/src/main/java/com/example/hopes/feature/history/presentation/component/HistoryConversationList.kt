package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.history.presentation.HistoryContentState
import com.example.hopes.feature.history.presentation.HistoryUiState

/** 서버 목록의 로딩·성공·빈 결과·오류 상태와 다음 페이지 로드를 표시한다. */
@Composable
fun HistoryConversationList(
    uiState: HistoryUiState,
    onChatClick: (Long) -> Unit,
    onRetryClick: () -> Unit,
    onLoadNextPage: () -> Unit,
    onLoadNextPageRetryClick: () -> Unit,
) {
    when (uiState.contentState) {
        HistoryContentState.Loading -> HistoryListStateMessage(
            text = stringResource(R.string.history_loading),
        )

        HistoryContentState.Empty -> HistoryListStateMessage(
            text = stringResource(R.string.history_empty),
        )

        HistoryContentState.Error -> HistoryListStateMessage(
            text = stringResource(R.string.history_error),
            onClick = onRetryClick,
        )

        HistoryContentState.Content -> HistoryConversationContent(
            uiState = uiState,
            onChatClick = onChatClick,
            onLoadNextPage = onLoadNextPage,
            onLoadNextPageRetryClick = onLoadNextPageRetryClick,
        )
    }
}

@Composable
private fun HistoryConversationContent(
    uiState: HistoryUiState,
    onChatClick: (Long) -> Unit,
    onLoadNextPage: () -> Unit,
    onLoadNextPageRetryClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val shouldLoadNextPage by remember(uiState.chats.size) {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
            lastVisibleIndex != null && lastVisibleIndex >= uiState.chats.lastIndex
        }
    }

    LaunchedEffect(shouldLoadNextPage, uiState.hasNextPage, uiState.isLoadingNextPage) {
        if (shouldLoadNextPage && uiState.hasNextPage && !uiState.isLoadingNextPage) {
            onLoadNextPage()
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(
                start = AppSpacing.ScreenHorizontal,
                top = HISTORY_LIST_TOP_PADDING,
                end = AppSpacing.ScreenHorizontal,
            )
            .fillMaxWidth()
            .padding(bottom = HISTORY_LIST_BOTTOM_PADDING),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        items(
            count = uiState.chats.size,
            key = { index -> uiState.chats[index].id },
        ) { index ->
            val chat = uiState.chats[index]
            HistoryItem(
                question = chat.title,
                onClick = { onChatClick(chat.id) },
            )
        }

        if (uiState.isLoadingNextPage) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppSpacing.Item),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        if (uiState.isNextPageError) {
            item {
                Text(
                    text = stringResource(R.string.history_load_next_page_error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onLoadNextPageRetryClick)
                        .padding(vertical = AppSpacing.Item),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun HistoryListStateMessage(
    text: String,
    onClick: (() -> Unit)? = null,
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(
                start = AppSpacing.ScreenHorizontal,
                top = HISTORY_LIST_TOP_PADDING,
                end = AppSpacing.ScreenHorizontal,
            )
            .fillMaxSize()
            .then(
                if (onClick == null) {
                    Modifier
                } else {
                    Modifier.clickable(onClick = onClick)
                },
            ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
    )
}

// "모든 기록" 라벨(약 20dp 높이) 아래 20dp 지점이다.
private val HISTORY_LIST_TOP_PADDING = HISTORY_ALL_RECORDS_TOP_PADDING + 15.dp + 20.dp
private val HISTORY_LIST_BOTTOM_PADDING = 0.dp
