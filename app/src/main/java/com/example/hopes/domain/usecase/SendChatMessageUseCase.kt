package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    /** 질문 전송 이벤트에서 공백·길이를 검증한 뒤 AI 응답 생성을 요청한다. */
    suspend operator fun invoke(chatId: Long, content: String): AppResult<Chat> {
        if (chatId <= 0 || content.isBlank() || content.length > MAX_MESSAGE_LENGTH) {
            return AppResult.Failure(AppError.Validation)
        }

        return chatRepository.sendMessage(chatId, content)
    }

    private companion object {
        const val MAX_MESSAGE_LENGTH = 12_000
    }
}
