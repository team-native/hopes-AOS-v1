package com.example.hopes.feature.history.presentation

sealed interface HistoryScreenEvent {
    data object NewChatClicked : HistoryScreenEvent
    data class SearchQueryChanged(val query: String) : HistoryScreenEvent
    data class ChatClicked(val chatId: Long) : HistoryScreenEvent
    data object RetryClicked : HistoryScreenEvent
    data object LoadNextPageClicked : HistoryScreenEvent
    data object LoadNextPageRetryClicked : HistoryScreenEvent
}
