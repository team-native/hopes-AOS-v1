package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(keyword: String?, page: Int, size: Int): AppResult<ChatPage> {
        return chatRepository.getChats(keyword, page, size)
    }
}

class GetChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId: Long, page: Int, size: Int): AppResult<Chat> {
        return chatRepository.getChat(chatId, page, size)
    }
}

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(title: String?): AppResult<Chat> {
        return chatRepository.createChat(title)
    }
}

class SendChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chatId: Long, content: String): AppResult<Chat> {
        return chatRepository.sendMessage(chatId, content)
    }
}
