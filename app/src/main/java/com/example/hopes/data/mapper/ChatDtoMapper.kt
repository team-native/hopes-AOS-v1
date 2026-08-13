package com.example.hopes.data.mapper

import com.example.hopes.data.api.ChatResponseDto
import com.example.hopes.data.api.MainResponseDto
import com.example.hopes.data.api.MessageDto
import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatMessage
import com.example.hopes.domain.model.ChatMessageRole
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.model.ChatSummary

fun MainResponseDto.toDomain(): ChatPage {
    return ChatPage(
        chats = chatList.map { chat ->
            ChatSummary(
                id = chat.id,
                title = chat.title,
                updatedAt = chat.updatedAt,
            )
        },
        searchKeyword = searchKeyword,
        page = page,
        size = size,
        hasNextPage = hasNext,
    )
}

fun ChatResponseDto.toDomain(): Chat {
    return Chat(
        id = id,
        title = title,
        messages = messages.map(MessageDto::toDomain),
        messagePage = messagePage,
        messageSize = messageSize,
        hasMoreMessages = hasMoreMessages,
    )
}

private fun MessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        role = when (role) {
            "USER" -> ChatMessageRole.User
            "ASSISTANT" -> ChatMessageRole.Assistant
            else -> ChatMessageRole.Unknown
        },
        content = content,
        createdAt = createdAt,
    )
}
