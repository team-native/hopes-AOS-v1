package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject
import java.util.UUID

class GetChatsUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(
            keyword: String?,
            page: Int,
            size: Int,
        ): AppResult<ChatPage> = chatRepository.getChats(keyword, page, size)
    }

/**
 * 내 대화가 모두 몇 개인지 센다. 목록 API에 전체 개수가 없어 마지막 페이지까지 넘기며 더한다.
 * 실패하면 그 결과를 그대로 돌려준다.
 */
class CountChatsUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(): AppResult<Int> {
            var count = 0
            var page = FIRST_PAGE
            while (page < FIRST_PAGE + MAX_PAGES) {
                when (val result = chatRepository.getChats(keyword = null, page = page, size = PAGE_SIZE)) {
                    is AppResult.Success -> {
                        count += result.value.chats.size
                        if (!result.value.hasNextPage) return AppResult.Success(count)
                    }

                    is AppResult.HttpError -> {
                        return result
                    }

                    AppResult.NetworkError -> {
                        return AppResult.NetworkError
                    }

                    AppResult.SerializationError -> {
                        return AppResult.SerializationError
                    }
                }
                page++
            }
            return AppResult.Success(count)
        }

        private companion object {
            const val FIRST_PAGE = 0
            const val PAGE_SIZE = 50

            // 서버가 hasNext를 계속 true로 주더라도 무한히 부르지 않게 막는다(최대 2,500개).
            const val MAX_PAGES = 50
        }
    }

class GetChatUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(
            chatId: Long,
            page: Int,
            size: Int,
        ): AppResult<Chat> = chatRepository.getChat(chatId, page, size)
    }

class CreateChatUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(title: String?): AppResult<Chat> = chatRepository.createChat(title)
    }

class SendChatMessageUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(
            chatId: Long,
            content: String,
            attachmentIds: List<Long> = emptyList(),
            departmentId: Long? = null,
            specialtyId: Long? = null,
            clientRequestId: String = UUID.randomUUID().toString(),
        ): AppResult<Chat> = chatRepository.sendMessage(chatId, content, attachmentIds, departmentId, specialtyId, clientRequestId)
    }

class DeleteChatUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        /** 사용자가 대화 기록에서 삭제를 확정했을 때 서버 기록을 삭제한다. */
        suspend operator fun invoke(chatId: Long): AppResult<Unit> = chatRepository.deleteChat(chatId)
    }
