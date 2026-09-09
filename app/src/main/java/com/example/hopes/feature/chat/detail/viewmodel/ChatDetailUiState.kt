package com.example.hopes.feature.chat.detail.viewmodel

/** 서버 대화 상세의 현재 표시 상태다. */
data class ChatDetailUiState(
    val title: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val replyText: String = "",
    val isLoading: Boolean = true,
    val isLoadError: Boolean = false,
    val isSending: Boolean = false,
    val isSendError: Boolean = false,
)

data class ChatMessageUiModel(
    val id: Long?,
    val content: String,
    val isUser: Boolean,
)
