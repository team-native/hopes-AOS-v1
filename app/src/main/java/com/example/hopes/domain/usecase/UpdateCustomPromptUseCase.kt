package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/**
 * 개인 설정의 시스템 프롬프트를 저장한다. Repository는 null을 "필드 생략"으로 직렬화하므로
 * (명시적 삭제로 동작하지 않음) 의도를 분명히 하기 위해 non-null String만 받는다.
 */
class UpdateCustomPromptUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(customPrompt: String): AppResult<UserSettings> =
        settingsRepository.updateCustomPrompt(customPrompt)
}
