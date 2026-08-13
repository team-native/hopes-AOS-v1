package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 사용자가 로그아웃을 확정했을 때 서버 토큰 폐기와 로컬 세션 종료를 요청한다. */
    suspend operator fun invoke(): AppResult<Unit> {
        return settingsRepository.logout()
    }
}
