package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 개인 설정 화면 진입 시 서버에 저장된 사용자 설정을 조회한다. */
    suspend operator fun invoke(): AppResult<UserSettings> = settingsRepository.getSettings()
}

class UpdateCustomPromptUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 개인 설정의 저장 버튼에서 호출되어 사용자 프롬프트를 서버에 반영한다. */
    suspend operator fun invoke(customPrompt: String?): AppResult<UserSettings> {
        return settingsRepository.updateCustomPrompt(customPrompt)
    }
}

class SubmitInquiryUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 문의하기의 전송 버튼에서 호출되어 문의 내용을 서버에 전달한다. */
    suspend operator fun invoke(content: String): AppResult<Unit> {
        return settingsRepository.submitInquiry(content)
    }
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
