package com.example.hopes.feature.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.hopes.navigation.HopesDestination

/** 채팅 입력 상태를 소유하고 화면 이벤트를 처리한다. */
@Composable
fun ChatRoute(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
) {
    var questionText by rememberSaveable { mutableStateOf("") }

    ChatScreen(
        questionText = questionText,
        onEvent = { event ->
            when (event) {
                is ChatScreenEvent.QuestionChanged -> questionText = event.question
                ChatScreenEvent.QuestionSubmitted -> {
                    questionText.trim()
                        .takeIf(String::isNotEmpty)
                        ?.let { question ->
                            questionText = ""
                            onNavigateToChatDetail(question)
                        }
                }
                is ChatScreenEvent.SuggestionSelected -> {
                    questionText = ""
                    onNavigateToChatDetail(event.question)
                }
                ChatScreenEvent.NewChatClicked -> questionText = ""
            }
        },
        onNavigate = onNavigate,
    )
}
