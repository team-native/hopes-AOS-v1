package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    // 마이페이지 진입 시 서버에 저장된 프로필을 불러온다.
    // 조회 실패 시 AppResult 오류를 호출한 ViewModel에 그대로 전달한다.
    suspend operator fun invoke(): AppResult<UserProfile> {
        return settingsRepository.getProfile()
    }
}
