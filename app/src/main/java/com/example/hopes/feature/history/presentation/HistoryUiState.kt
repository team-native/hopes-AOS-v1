package com.example.hopes.feature.history.presentation

data class HistoryUiState(
    val searchQuery: String = "",
    val chats: List<ChatSummaryUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val isError: Boolean = false,
)

data class ChatSummaryUiModel(
    val id: Long,
    val title: String,
)
