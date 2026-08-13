package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 설정 화면 진입 시 계정·테마·개인 프롬프트를 함께 불러온다. */
    suspend operator fun invoke(): AppResult<UserSettings> {
        return settingsRepository.getSettings()
    }
}
