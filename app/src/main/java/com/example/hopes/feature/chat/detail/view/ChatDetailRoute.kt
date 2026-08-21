package com.example.hopes.feature.chat.detail.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailViewModel
import com.example.hopes.navigation.HopesDestination

/** 서버 대화 상세 상태를 수집하고 탐색 이벤트를 연결한다. */
@Composable
fun ChatDetailRoute(
    chatId: Long,
    question: String,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(chatId, question) { viewModel.initialize(chatId, question) }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ChatDetailScreen(
        uiState = uiState.value,
        onEvent = { event ->
            if (event == ChatDetailScreenEvent.BackClicked) {
                onBackClick()
            } else {
                viewModel.onEvent(event)
            }
        },
        onNavigate = onNavigate,
    )
}
