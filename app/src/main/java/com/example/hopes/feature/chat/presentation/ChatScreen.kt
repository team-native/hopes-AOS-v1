package com.example.hopes.feature.chat.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.chat.presentation.content.ChatScreenContent
import com.example.hopes.navigation.HopesDestination

/** 채팅 상태와 이벤트를 화면 콘텐츠로 전달한다. */
@Composable
fun ChatScreen(
    questionText: String,
    submittedQuestion: String?,
    onEvent: (ChatScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    ChatScreenContent(
        questionText = questionText,
        submittedQuestion = submittedQuestion,
        onQuestionChange = { onEvent(ChatScreenEvent.QuestionChanged(it)) },
        onSuggestionClick = { onEvent(ChatScreenEvent.SuggestionSelected(it)) },
        onSubmitClick = { onEvent(ChatScreenEvent.QuestionSubmitted) },
        onNavigate = onNavigate,
    )
}
