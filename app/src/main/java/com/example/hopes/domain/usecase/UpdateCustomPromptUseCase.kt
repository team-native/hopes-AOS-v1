package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class UpdateCustomPromptUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 개인 설정 저장 이벤트에서 최대 4,000자 프롬프트를 서버에 저장한다. */
    suspend operator fun invoke(customPrompt: String): AppResult<UserSettings> {
        if (customPrompt.length > MAX_PROMPT_LENGTH) {
            return AppResult.Failure(AppError.Validation)
        }

        return settingsRepository.updateCustomPrompt(customPrompt)
    }

    private companion object {
        const val MAX_PROMPT_LENGTH = 4_000
    }
}
