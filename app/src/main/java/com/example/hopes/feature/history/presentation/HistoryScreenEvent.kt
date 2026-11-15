package com.example.hopes.feature.history.presentation

sealed interface HistoryScreenEvent {
    data object NewChatClicked : HistoryScreenEvent

    data class SearchQueryChanged(
        val query: String,
    ) : HistoryScreenEvent

    data class ChatClicked(
        val chatId: Long,
    ) : HistoryScreenEvent

    data object RetryClicked : HistoryScreenEvent

    data object LoadNextPageClicked : HistoryScreenEvent

    data object LoadNextPageRetryClicked : HistoryScreenEvent

    /** 행을 밀어 나온 "삭제"를 눌렀다. 확인 다이얼로그를 띄운다(피그마 24-5). */
    data class DeleteChatClicked(
        val chatId: Long,
    ) : HistoryScreenEvent

    data object DeleteChatConfirmed : HistoryScreenEvent

    data object DeleteChatDismissed : HistoryScreenEvent
}
