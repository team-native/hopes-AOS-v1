package com.example.hopes.feature.chat.presentation

sealed interface ChatScreenEvent {
    data class QuestionChanged(val question: String) : ChatScreenEvent
    data object QuestionSubmitted : ChatScreenEvent
    data object NewChatClicked : ChatScreenEvent
}
