package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/** 문의 내용과 답변 이메일을 분리해 서버로 전송한다. */
class SubmitInquiryUseCase
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            message: String,
        ): AppResult<Unit> = settingsRepository.submitInquiry(email, message)
    }
