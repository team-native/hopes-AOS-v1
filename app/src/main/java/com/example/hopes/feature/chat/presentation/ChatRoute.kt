package com.example.hopes.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hopes.navigation.HopesDestination

/** 채팅 입력 상태를 소유하고 화면 이벤트를 처리한다. */
@Composable
fun ChatRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            if (effect is ChatEffect.ChatCreated) {
                onNavigateToChatDetail(effect.question)
            }
        }
    }

    ChatScreen(
        questionText = uiState.value.questionText,
        onEvent = viewModel::onEvent,
        onNavigate = onNavigate,
    )
}
