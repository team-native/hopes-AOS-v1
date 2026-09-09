package com.example.hopes.feature.history.presentation

data class HistoryUiState(
    val searchQuery: String = "",
    val chats: List<ChatSummaryUiModel> = emptyList(),
    val contentState: HistoryContentState = HistoryContentState.Loading,
    val nextPage: Int = FIRST_PAGE,
    val hasNextPage: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val isNextPageError: Boolean = false,
)

data class ChatSummaryUiModel(
    val id: Long,
    val title: String,
)

sealed interface HistoryContentState {
    data object Loading : HistoryContentState
    data object Content : HistoryContentState
    data object Empty : HistoryContentState
    data object Error : HistoryContentState
}

const val FIRST_PAGE = 0
