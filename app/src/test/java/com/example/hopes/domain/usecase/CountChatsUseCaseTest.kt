package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.model.ChatSummary
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CountChatsUseCaseTest {
    @Test
    fun countsChatsAcrossAllPages() =
        runBlocking {
            val repository = FakeChatRepository(pages = listOf(50, 50, 7))

            val result = CountChatsUseCase(repository)()

            assertEquals(AppResult.Success(107), result)
            assertEquals(listOf(0, 1, 2), repository.requestedPages)
        }

    @Test
    fun noChats_returnsZero() =
        runBlocking {
            val result = CountChatsUseCase(FakeChatRepository(pages = listOf(0)))()

            assertEquals(AppResult.Success(0), result)
        }

    @Test
    fun failureOnLaterPage_returnsFailure() =
        runBlocking {
            val repository = FakeChatRepository(pages = listOf(50), failOnPage = 1)

            val result = CountChatsUseCase(repository)()

            assertEquals(AppResult.NetworkError, result)
        }

    private class FakeChatRepository(
        private val pages: List<Int>,
        private val failOnPage: Int? = null,
    ) : ChatRepository {
        val requestedPages = mutableListOf<Int>()

        override suspend fun getChats(
            keyword: String?,
            page: Int,
            size: Int,
        ): AppResult<ChatPage> {
            requestedPages += page
            if (page == failOnPage) return AppResult.NetworkError
            val count = pages.getOrElse(page) { 0 }
            return AppResult.Success(
                ChatPage(
                    chats = List(count) { ChatSummary(id = (page * size + it).toLong(), title = "", updatedAt = "") },
                    hasNextPage = page < pages.lastIndex || page == failOnPage?.minus(1),
                ),
            )
        }

        override suspend fun getChat(
            id: Long,
            page: Int,
            size: Int,
        ): AppResult<Chat> = error("not used")

        override suspend fun createChat(title: String?): AppResult<Chat> = error("not used")

        override suspend fun sendMessage(
            id: Long,
            content: String,
        ): AppResult<Chat> = error("not used")
    }
}
