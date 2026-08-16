package com.example.hopes.feature.chat.presentation.detail

import androidx.compose.runtime.Composable
import com.example.hopes.feature.chat.presentation.detail.content.ChatDetailScreenContent
import com.example.hopes.navigation.HopesDestination

/** 서버 대화 상세 상태를 콘텐츠에 전달한다. */
@Composable
fun ChatDetailScreen(
    uiState: ChatDetailUiState,
    onEvent: (ChatDetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    ChatDetailScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onNavigate = onNavigate,
    )
}
