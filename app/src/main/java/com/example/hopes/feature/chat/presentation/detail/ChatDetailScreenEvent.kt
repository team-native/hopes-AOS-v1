package com.example.hopes.feature.chat.presentation.detail

sealed interface ChatDetailScreenEvent {
    data object BackClicked : ChatDetailScreenEvent
    data object RetryClicked : ChatDetailScreenEvent
    data class ReplyChanged(val value: String) : ChatDetailScreenEvent
    data object ReplySubmitted : ChatDetailScreenEvent
}
