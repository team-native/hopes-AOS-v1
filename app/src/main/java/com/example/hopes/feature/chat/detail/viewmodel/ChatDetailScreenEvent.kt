package com.example.hopes.feature.chat.detail.viewmodel

sealed interface ChatDetailScreenEvent {
    data object BackClicked : ChatDetailScreenEvent

    /** 기존 대화 불러오기를 다시 시도한다. */
    data object RetryClicked : ChatDetailScreenEvent

    /** 답변을 받지 못한 질문을 같은 내용으로 다시 보낸다. */
    data object ResendClicked : ChatDetailScreenEvent

    data class ReplyChanged(
        val value: String,
    ) : ChatDetailScreenEvent

    data object ReplySubmitted : ChatDetailScreenEvent
}
