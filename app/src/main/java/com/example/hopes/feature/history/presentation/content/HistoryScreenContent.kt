package com.example.hopes.feature.history.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.history.presentation.HistoryScreenEvent
import com.example.hopes.feature.history.presentation.HistoryUiState
import com.example.hopes.feature.history.presentation.component.HistoryConversationList
import com.example.hopes.feature.history.presentation.component.HistoryHeader
import com.example.hopes.feature.history.presentation.component.HistoryNewChatButton
import com.example.hopes.feature.history.presentation.component.HistorySearchField
import com.example.hopes.navigation.HopesDestination

/** 지난 대화의 헤더·검색·목록 컴포넌트를 Figma 기록 화면으로 조합한다. */
@Composable
fun HistoryScreenContent(
    uiState: HistoryUiState,
    onEvent: (HistoryScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.History,
        onNavigate = onNavigate,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HistoryHeader()
            HistoryNewChatButton(
                onClick = { onEvent(HistoryScreenEvent.NewChatClicked) },
            )
            HistorySearchField(
                value = uiState.searchQuery,
                onValueChange = { query ->
                    onEvent(HistoryScreenEvent.SearchQueryChanged(query))
                },
            )
            HistoryConversationList(
                uiState = uiState,
                onChatClick = { chatId ->
                    onEvent(HistoryScreenEvent.ChatClicked(chatId))
                },
                onRetryClick = { onEvent(HistoryScreenEvent.RetryClicked) },
                onLoadNextPage = { onEvent(HistoryScreenEvent.LoadNextPageClicked) },
                onLoadNextPageRetryClick = {
                    onEvent(HistoryScreenEvent.LoadNextPageRetryClicked)
                },
            )
        }
    }
}
