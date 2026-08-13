package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.AppTheme
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class UpdateThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 테마 토글 이벤트에서 서버가 지원하는 테마만 저장한다. */
    suspend operator fun invoke(theme: AppTheme): AppResult<AppTheme> {
        if (theme == AppTheme.Unknown) {
            return AppResult.Failure(AppError.Validation)
        }

        return settingsRepository.updateTheme(theme)
    }
}
