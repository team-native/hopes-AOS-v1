package com.example.hopes.feature.history.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 기록 서버 상태를 수집하고 상세 이동을 연결한다. */
@Composable
fun HistoryRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToChatDetail: (Long) -> Unit,
    onStartNewChat: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    HistoryScreen(
        uiState = uiState.value,
        onEvent = { event ->
            when (event) {
                HistoryScreenEvent.NewChatClicked -> onStartNewChat()
                is HistoryScreenEvent.ChatClicked -> onNavigateToChatDetail(event.chatId)
                else -> viewModel.onEvent(event)
            }
        },
        onNavigate = onNavigate,
    )
}
