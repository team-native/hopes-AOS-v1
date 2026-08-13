package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.ChatPage
import com.example.hopes.domain.repository.ChatRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    /** 기록 화면 진입·검색·더보기 요청에서 대화 목록을 읽는다. */
    suspend operator fun invoke(keyword: String?, page: Int, size: Int): AppResult<ChatPage> {
        if (page < 0 || size !in 1..100 || (keyword?.length ?: 0) > MAX_SEARCH_LENGTH) {
            return AppResult.Failure(AppError.Validation)
        }

        return chatRepository.getChats(keyword, page, size)
    }

    private companion object {
        const val MAX_SEARCH_LENGTH = 255
    }
}
