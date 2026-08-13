package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class SubmitInquiryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 문의 전송 이벤트에서 공백·최대 길이를 검증한 뒤 접수를 요청한다. */
    suspend operator fun invoke(content: String): AppResult<Unit> {
        if (content.isBlank() || content.length > MAX_INQUIRY_LENGTH) {
            return AppResult.Failure(AppError.Validation)
        }

        return settingsRepository.submitInquiry(content)
    }

    private companion object {
        const val MAX_INQUIRY_LENGTH = 4_000
    }
}
