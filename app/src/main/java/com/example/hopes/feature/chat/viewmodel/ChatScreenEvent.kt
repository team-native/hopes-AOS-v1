package com.example.hopes.feature.chat.viewmodel

sealed interface ChatScreenEvent {
    data class QuestionChanged(val question: String) : ChatScreenEvent
    data object QuestionSubmitted : ChatScreenEvent
    data class SuggestionClicked(val question: String) : ChatScreenEvent
    data object NewChatClicked : ChatScreenEvent
}
