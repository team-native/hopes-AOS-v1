package com.example.hopes.feature.history.presentation

import java.time.LocalDate

data class HistoryUiState(
    val searchQuery: String = "",
    val chats: List<ChatSummaryUiModel> = emptyList(),
    val contentState: HistoryContentState = HistoryContentState.Loading,
    val nextPage: Int = FIRST_PAGE,
    val hasNextPage: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val isNextPageError: Boolean = false,
    val pendingDeleteChatId: Long? = null,
)

/**
 * @param date 마지막으로 이어서 이야기한 날짜다. 서버 값을 읽지 못하면 null이다.
 */
data class ChatSummaryUiModel(
    val id: Long,
    val title: String,
    val date: LocalDate? = null,
)

sealed interface HistoryContentState {
    data object Loading : HistoryContentState

    data object Content : HistoryContentState

    data object Empty : HistoryContentState

    data object Error : HistoryContentState
}

const val FIRST_PAGE = 0
