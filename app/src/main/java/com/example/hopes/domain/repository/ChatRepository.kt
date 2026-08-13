package com.example.hopes.domain.repository

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.result.AppResult

interface ChatRepository {
    suspend fun getChats(keyword: String?, page: Int, size: Int): AppResult<ChatPage>

    suspend fun getChat(chatId: Long, page: Int, size: Int): AppResult<Chat>

    suspend fun createChat(title: String?): AppResult<Chat>

    suspend fun sendMessage(chatId: Long, content: String): AppResult<Chat>
}
