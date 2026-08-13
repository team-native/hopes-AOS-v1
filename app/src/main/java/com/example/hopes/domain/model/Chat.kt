package com.example.hopes.domain.model

data class ChatSummary(
    val id: Long,
    val title: String,
    val updatedAt: String,
)

data class ChatMessage(
    val id: Long?,
    val role: String,
    val content: String,
    val createdAt: String?,
)

data class Chat(
    val id: Long,
    val title: String,
    val messages: List<ChatMessage>,
    val hasMoreMessages: Boolean,
)

data class ChatPage(
    val chats: List<ChatSummary>,
    val hasNextPage: Boolean,
)
