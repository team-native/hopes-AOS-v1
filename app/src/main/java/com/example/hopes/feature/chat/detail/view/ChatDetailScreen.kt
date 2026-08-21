package com.example.hopes.feature.chat.detail.view

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.chat.detail.view.component.ChatDetailMessageList
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaChatReplyBar
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.navigation.HopesDestination

/** 서버 메시지 목록과 추가 질문 입력을 피그마 상세 화면에 표시한다. */
@Composable
fun ChatDetailScreen(
    uiState: ChatDetailUiState,
    onEvent: (ChatDetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
        fixedTopContent = {
            FigmaDetailBackHeader(
                title = uiState.title.ifBlank { stringResource(R.string.chat_detail_title) },
                subtitle = stringResource(R.string.chat_answer_label),
                onBackClick = { onEvent(ChatDetailScreenEvent.BackClicked) },
                actionText = stringResource(R.string.chat_save),
                onActionClick = {},
                applySystemBarPadding = true,
                subtitleSpacing = 3.dp,
            )
        },
        fixedBottomContent = {
            FigmaChatReplyBar(
                value = uiState.replyText,
                onValueChange = { onEvent(ChatDetailScreenEvent.ReplyChanged(it)) },
                onSubmitClick = { onEvent(ChatDetailScreenEvent.ReplySubmitted) },
                modifier = Modifier,
            )
        },
        isBottomNavigationVisible = !isImeVisible,
    ) {
        ChatDetailMessageList(
            uiState = uiState,
            onRetryClick = { onEvent(ChatDetailScreenEvent.RetryClicked) },
        )
    }
}
