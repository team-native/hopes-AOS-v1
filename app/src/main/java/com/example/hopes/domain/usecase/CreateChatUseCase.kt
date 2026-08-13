package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.Chat
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    /** 새 질문을 보낼 대화를 만들며 제목 길이를 명세 범위로 제한한다. */
    suspend operator fun invoke(title: String?): AppResult<Chat> {
        if ((title?.length ?: 0) > MAX_TITLE_LENGTH) {
            return AppResult.Failure(AppError.Validation)
        }

        return chatRepository.createChat(title?.takeIf(String::isNotBlank))
    }

    private companion object {
        const val MAX_TITLE_LENGTH = 255
    }
}
