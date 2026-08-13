package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): AppResult<UserSettings> = settingsRepository.getSettings()
}

class UpdateThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(theme: String): AppResult<String> = settingsRepository.updateTheme(theme)
}

class LogoutUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): AppResult<Unit> = settingsRepository.logout()
}
