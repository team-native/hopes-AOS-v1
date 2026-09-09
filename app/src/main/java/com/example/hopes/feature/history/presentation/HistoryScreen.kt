package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.history.presentation.content.HistoryScreenContent
import com.example.hopes.navigation.HopesDestination

/** 기록 상태와 단일 화면 이벤트를 콘텐츠로 전달한다. */
@Composable
fun HistoryScreen(
    uiState: HistoryUiState,
    onEvent: (HistoryScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HistoryScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onNavigate = onNavigate,
    )
}
