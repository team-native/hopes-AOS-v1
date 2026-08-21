package com.example.hopes.feature.chat.detail.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailUiState
import com.example.hopes.feature.chat.view.component.ChatMessageStateText
import com.example.hopes.feature.detail.presentation.component.FigmaChatAnswerGeneratingBubble
import com.example.hopes.feature.detail.presentation.component.FigmaChatDetailBubble

/** 서버 메시지 목록을 표시하고, 목록이 바뀌거나 전송 중 표시가 나타나면 맨 아래로 자동 스크롤한다. */
@Composable
fun ChatDetailMessageList(
    uiState: ChatDetailUiState,
    onRetryClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    // 메시지 목록이 바뀌거나 전송 중 표시가 나타날 때 맨 아래로 스크롤한다.
    LaunchedEffect(uiState.messages, uiState.isSending) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .padding(
                start = 24.dp,
                // 상세 헤더의 subtitle과 첫 채팅 메시지 사이를 20.dp로 유지한다.
                top = AppSpacing.ScreenVertical,
                end = 24.dp,
                bottom = 16.dp,
            )
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when {
            uiState.isLoading -> ChatMessageStateText(R.string.chat_messages_loading)
            uiState.isLoadError -> ChatMessageStateText(R.string.chat_messages_error, onRetryClick)
            uiState.messages.isEmpty() -> Unit
            else -> uiState.messages.forEach { message ->
                FigmaChatDetailBubble(
                    text = message.content,
                    modifier = Modifier.align(if (message.isUser) Alignment.End else Alignment.Start),
                    isUser = message.isUser,
                )
            }
        }
        if (uiState.isSending) {
            FigmaChatAnswerGeneratingBubble(modifier = Modifier.align(Alignment.Start))
        }
        if (uiState.isSendError) {
            ChatMessageStateText(R.string.chat_message_send_error)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
