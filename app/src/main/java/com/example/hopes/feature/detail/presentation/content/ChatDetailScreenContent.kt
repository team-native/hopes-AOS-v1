package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaChatDetailBubble
import com.example.hopes.feature.detail.presentation.component.FigmaChatReplyBar
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.feature.detail.presentation.component.FigmaImeChatReplyBar
import com.example.hopes.navigation.HopesDestination

/** 피그마 06 채팅 상세를 로컬 대화·저장·추가 질문 상태로 구성한다. */
@Composable
fun ChatDetailScreenContent(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val conversation = uiState.conversation
    val isSaved = conversation?.isSaved == true
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    FigmaAppFrame(
        selectedDestination = HopesDestination.Chat,
        onNavigate = onNavigate,
        imeOverlay = { viewportMetrics ->
            if (isImeVisible) {
                FigmaImeChatReplyBar(
                    viewportMetrics = viewportMetrics,
                    value = conversation?.replyDraft.orEmpty(),
                    onValueChange = { onEvent(DetailScreenEvent.ReplyChanged(it)) },
                    onSubmitClick = { onEvent(DetailScreenEvent.ReplySubmitted) },
                )
            }
        },
    ) {
        FigmaDetailBackHeader(
            title = stringResource(R.string.chat_detail_title),
            subtitle = stringResource(R.string.chat_answer_label),
            onBackClick = { onEvent(DetailScreenEvent.BackClicked) },
            actionText = if (isSaved) {
                stringResource(R.string.saved)
            } else {
                stringResource(R.string.chat_save)
            },
            onActionClick = { onEvent(DetailScreenEvent.ConversationSaveClicked) },
            backOffsetX = 19,
        )
        FigmaChatDetailBubble(
            text = conversation?.question.orEmpty().ifBlank {
                stringResource(R.string.chat_detail_title)
            },
            modifier = Modifier.padding(start = 113.dp, top = 150.dp),
            isUser = true,
        )
        FigmaChatDetailBubble(
            text = stringResource(R.string.chat_detail_answer),
            modifier = Modifier.padding(start = 24.dp, top = 232.dp),
            isUser = false,
        )
        // 전송한 추가 질문은 초기 Figma 레이아웃 아래의 빈 대화 영역에 로컬로 누적한다.
        conversation?.replies?.forEachIndexed { replyIndex, submittedReply ->
            FigmaChatDetailBubble(
                text = submittedReply,
                modifier = Modifier.padding(start = 113.dp, top = (368 + (replyIndex * 70)).dp),
                isUser = true,
            )
        }
        if (!isImeVisible) {
            FigmaChatReplyBar(
                value = conversation?.replyDraft.orEmpty(),
                onValueChange = { onEvent(DetailScreenEvent.ReplyChanged(it)) },
                onSubmitClick = { onEvent(DetailScreenEvent.ReplySubmitted) },
                modifier = Modifier.padding(top = 716.dp),
            )
        }
    }
}
