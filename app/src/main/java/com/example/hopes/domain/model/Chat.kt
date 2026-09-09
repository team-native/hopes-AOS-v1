package com.example.hopes.domain.model

data class ChatSummary(
    val id: Long,
    val title: String,
    val updatedAt: String,
)

data class ChatMessage(
    val id: Long?,
    val role: ChatMessageRole,
    val content: String,
    val createdAt: String?,
)

/** 서버 메시지 발신자를 앱의 표시 규칙과 분리해 표현한다. */
enum class ChatMessageRole {
    User,
    Assistant,
    Unknown,
}

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
