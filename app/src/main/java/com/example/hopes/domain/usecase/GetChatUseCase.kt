package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    /** 대화 상세 화면 진입 시 지정한 메시지 페이지를 불러온다. */
    suspend operator fun invoke(chatId: Long, page: Int, size: Int): AppResult<Chat> {
        if (chatId <= 0 || page < 0 || size !in 1..100) {
            return AppResult.Failure(AppError.Validation)
        }

        return chatRepository.getChat(chatId, page, size)
    }
}
