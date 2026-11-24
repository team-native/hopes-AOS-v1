package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SendChatMessageUseCaseTest {
    @Test
    fun `재시도에 전달된 clientRequestId를 저장소 호출마다 그대로 전달한다`() = runBlocking {
        val repository = RecordingChatRepository()
        val useCase = SendChatMessageUseCase(repository)

        useCase(chatId = 7, content = "질문", clientRequestId = "stable-request-id")
        useCase(chatId = 7, content = "질문", clientRequestId = "stable-request-id")

        assertEquals(listOf("stable-request-id", "stable-request-id"), repository.requestIds)
    }

    private class RecordingChatRepository : ChatRepository {
        val requestIds = mutableListOf<String>()

        override suspend fun getChats(keyword: String?, page: Int, size: Int): AppResult<ChatPage> = error("unused")
        override suspend fun getChat(id: Long, page: Int, size: Int): AppResult<Chat> = error("unused")
        override suspend fun createChat(title: String?): AppResult<Chat> = error("unused")
        override suspend fun sendMessage(id: Long, content: String, attachmentIds: List<Long>, departmentId: Long?, specialtyId: Long?, clientRequestId: String): AppResult<Chat> {
            requestIds += clientRequestId
            return AppResult.Success(Chat(id, "", emptyList(), false))
        }
        override suspend fun deleteChat(id: Long): AppResult<Unit> = error("unused")
    }
}
