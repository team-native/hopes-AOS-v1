package com.example.hopes.feature.chat.presentation

data class ChatUiState(
    val questionText: String = "",
    val isLoading: Boolean = false,
    val isCreateChatError: Boolean = false,
)
