package com.example.hopes.feature.chat.detail.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.feature.chat.detail.view.component.ChatAnswerCard
import com.example.hopes.feature.chat.detail.view.component.ChatAnswerGenerating
import com.example.hopes.feature.chat.detail.view.component.ChatErrorMessage
import com.example.hopes.feature.chat.detail.view.component.ChatUserBubble
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailUiState
import com.example.hopes.feature.chat.detail.viewmodel.ChatMessageUiModel
import com.example.hopes.feature.chat.view.component.ChatComposer
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 08 채팅 상세 화면이다. 질문 말풍선과 문단 정리된 답변 카드를 쌓고, 하단에 추가 질문 입력창을 둔다.
 *
 * @param subtitle 헤더 제목 아래 문구다(예: "소프트웨어개발과 · Frontend").
 * @param toastMessage 복사 완료처럼 잠깐 띄울 안내다.
 */
@Composable
fun ChatDetailScreen(
    uiState: ChatDetailUiState,
    subtitle: String,
    onEvent: (ChatDetailScreenEvent) -> Unit,
    onCopyAnswer: (String) -> Unit,
    onShareAnswer: (String) -> Unit,
    onCopyCode: (String) -> Unit,
    toastMessage: String? = null,
    onToastDismiss: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    // 메시지가 늘거나 답변 생성·실패 표시가 바뀌면 맨 아래로 스크롤한다.
    LaunchedEffect(uiState.messages, uiState.isSending, uiState.failedQuestion) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    HopesSubScreenFrame(
        title = uiState.title.ifBlank { stringResource(R.string.chat_detail_title) },
        subtitle = subtitle,
        onBackClick = { onEvent(ChatDetailScreenEvent.BackClicked) },
        scrollState = scrollState,
        bottomButton = {
            ChatComposer(
                value = uiState.replyText,
                onValueChange = { onEvent(ChatDetailScreenEvent.ReplyChanged(it)) },
                onSubmitClick = { onEvent(ChatDetailScreenEvent.ReplySubmitted) },
                placeholder =
                    if (uiState.isSending) {
                        stringResource(R.string.chat_generating_placeholder)
                    } else {
                        stringResource(R.string.chat_additional_question)
                    },
                isEnabled = uiState.isReplyEnabled,
            )
        },
        toast =
            toastMessage?.let { message ->
                { HopesToast(message = message, onDismiss = onToastDismiss) }
            },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            when {
                uiState.isLoading -> {
                    Text(
                        text = stringResource(R.string.chat_messages_loading),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = HopesTextStyles.Caption,
                    )
                }

                uiState.isLoadError -> {
                    ChatErrorMessage(
                        title = stringResource(R.string.chat_load_failed_title),
                        description = stringResource(R.string.chat_load_failed_description),
                        onRetryClick = { onEvent(ChatDetailScreenEvent.RetryClicked) },
                    )
                }

                else -> {
                    uiState.messages.forEach { message ->
                        if (message.isUser) {
                            ChatUserBubble(
                                text = message.content,
                                modifier = Modifier.align(Alignment.End),
                            )
                        } else {
                            ChatAnswerCard(
                                text = message.content,
                                onCopyClick = { onCopyAnswer(message.content) },
                                onShareClick = { onShareAnswer(message.content) },
                                onCopyCode = onCopyCode,
                            )
                        }
                    }

                    if (uiState.isSending) {
                        ChatAnswerGenerating()
                    }

                    if (uiState.failedQuestion != null) {
                        ChatErrorMessage(
                            title = stringResource(R.string.chat_answer_failed_title),
                            description = stringResource(R.string.chat_answer_failed_description),
                            onRetryClick = { onEvent(ChatDetailScreenEvent.ResendClicked) },
                        )
                    }
                }
            }
        }
    }
}

private val previewAnswer =
    """
    # Frontend 시작 순서
    처음에는 화면을 직접 만들어 보는 것부터 시작하는 걸 추천해요.
    1. HTML·CSS로 간단한 페이지 만들기
    2. JavaScript 기초 문법 익히기
    3. React로 작은 프로젝트 완성하기
    ```html
    <button class="primary">질문하기</button>
    ```
    > 막히면 선배에게 바로 물어보는 게 제일 빨라요.

    [학교 전공 소개 페이지 보기](https://example.com)
    """.trimIndent()

@Preview(showBackground = true, name = "08 채팅 상세")
@Composable
private fun ChatDetailScreenPreview() {
    HopesTheme {
        ChatDetailScreen(
            uiState =
                ChatDetailUiState(
                    title = "전공 공부",
                    isLoading = false,
                    messages =
                        listOf(
                            ChatMessageUiModel(id = 1, content = "프론트엔드는 뭐부터 공부하면 좋아?", isUser = true),
                            ChatMessageUiModel(id = 2, content = previewAnswer, isUser = false),
                        ),
                ),
            subtitle = "소프트웨어개발과 · Frontend",
            onEvent = {},
            onCopyAnswer = {},
            onShareAnswer = {},
            onCopyCode = {},
        )
    }
}

@Preview(showBackground = true, name = "08-3 답변 생성 실패")
@Composable
private fun ChatDetailScreenFailedPreview() {
    HopesTheme {
        ChatDetailScreen(
            uiState =
                ChatDetailUiState(
                    title = "전공 공부",
                    isLoading = false,
                    messages = listOf(ChatMessageUiModel(id = null, content = "프론트엔드는 뭐부터 공부하면 좋아?", isUser = true)),
                    failedQuestion = "프론트엔드는 뭐부터 공부하면 좋아?",
                ),
            subtitle = "소프트웨어개발과 · Frontend",
            onEvent = {},
            onCopyAnswer = {},
            onShareAnswer = {},
            onCopyCode = {},
        )
    }
}
