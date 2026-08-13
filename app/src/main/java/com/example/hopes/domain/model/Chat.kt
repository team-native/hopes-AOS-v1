package com.example.hopes.domain.model

/** 서버가 부여하는 대화 식별자와 표시 정보를 담는다. */
data class ChatSummary(
    val id: Long,
    val title: String,
    val updatedAt: String,
)

/** 대화 메시지의 작성 주체다. */
enum class ChatMessageRole {
    User,
    Assistant,
    Unknown,
}

/** 대화 상세 화면에 표시하는 하나의 메시지다. */
data class ChatMessage(
    val id: Long?,
    val role: ChatMessageRole,
    val content: String,
    val createdAt: String?,
)

/** 메시지 페이지를 포함한 서버 대화 상태다. */
data class Chat(
    val id: Long,
    val title: String,
    val messages: List<ChatMessage>,
    val messagePage: Int,
    val messageSize: Int,
    val hasMoreMessages: Boolean,
)

/** 검색 조건과 다음 페이지 존재 여부를 포함한 대화 목록 결과다. */
data class ChatPage(
    val chats: List<ChatSummary>,
    val searchKeyword: String?,
    val page: Int,
    val size: Int,
    val hasNextPage: Boolean,
)
