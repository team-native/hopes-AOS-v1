package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/** 마이페이지 진입 시 서버에 저장된 프로필을 조회한다. */
class GetProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): AppResult<UserProfile> = settingsRepository.getProfile()
}
