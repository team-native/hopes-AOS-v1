package com.example.hopes.feature.chat.presentation.detail.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.chat.presentation.detail.ChatDetailScreenEvent
import com.example.hopes.feature.chat.presentation.detail.ChatDetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaChatDetailBubble
import com.example.hopes.feature.detail.presentation.component.FigmaChatReplyBar
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.navigation.HopesDestination

/** 서버 메시지 목록과 추가 질문 입력을 피그마 상세 화면에 표시한다. */
@Composable
fun ChatDetailScreenContent(
    uiState: ChatDetailUiState,
    onEvent: (ChatDetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
        fixedBottomContent = {
            FigmaChatReplyBar(
                value = uiState.replyText,
                onValueChange = { onEvent(ChatDetailScreenEvent.ReplyChanged(it)) },
                onSubmitClick = { onEvent(ChatDetailScreenEvent.ReplySubmitted) },
                modifier = Modifier.imePadding(),
            )
        },
    ) {
        FigmaDetailBackHeader(
            title = uiState.title.ifBlank { stringResource(R.string.chat_detail_title) },
            subtitle = stringResource(R.string.chat_answer_label),
            onBackClick = { onEvent(ChatDetailScreenEvent.BackClicked) },
            actionText = stringResource(R.string.chat_save),
            onActionClick = {},
            backOffsetX = 19,
        )
        ChatMessages(
            uiState = uiState,
            onRetryClick = { onEvent(ChatDetailScreenEvent.RetryClicked) },
        )
    }
}

@Composable
private fun ChatMessages(
    uiState: ChatDetailUiState,
    onRetryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp, top = 150.dp, end = 24.dp, bottom = 90.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when {
            uiState.isLoading -> MessageStateText(R.string.chat_messages_loading)
            uiState.isLoadError -> MessageStateText(R.string.chat_messages_error, onRetryClick)
            uiState.messages.isEmpty() -> MessageStateText(R.string.chat_messages_empty)
            else -> uiState.messages.forEach { message ->
                FigmaChatDetailBubble(
                    text = message.content,
                    modifier = Modifier.align(if (message.isUser) Alignment.End else Alignment.Start),
                    isUser = message.isUser,
                )
            }
        }
        if (uiState.isSendError) {
            MessageStateText(R.string.chat_message_send_error)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MessageStateText(
    textResId: Int,
    onClick: (() -> Unit)? = null,
) {
    Text(
        text = stringResource(textResId),
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick == null) Modifier else Modifier.clickable(onClick = onClick)),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
}
