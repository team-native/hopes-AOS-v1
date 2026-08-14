package com.example.hopes.feature.history.presentation

sealed interface HistoryScreenEvent {
    data class SearchQueryChanged(val query: String) : HistoryScreenEvent
    data class ChatClicked(val chatId: Long) : HistoryScreenEvent
    data object RetryClicked : HistoryScreenEvent
}
