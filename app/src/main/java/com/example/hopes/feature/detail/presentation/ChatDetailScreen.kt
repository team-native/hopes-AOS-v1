package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.detail.presentation.content.ChatDetailScreenContent
import com.example.hopes.navigation.HopesDestination

/** 채팅 상세 상태와 사용자 의도를 콘텐츠로 전달한다. */
@Composable
fun ChatDetailScreen(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    ChatDetailScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onNavigate = onNavigate,
    )
}
