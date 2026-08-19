package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/**
 * 문의 내용을 서버로 전송한다. 서버 API는 content 필드 하나만 받으므로,
 * 보낸 사람을 식별할 수 있도록 이메일을 문의 내용 앞에 합쳐 전송한다.
 */
class SubmitInquiryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(email: String, message: String): AppResult<Unit> =
        settingsRepository.submitInquiry("이메일: $email\n$message")
}
