package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

/** 마이페이지에서 수정한 프로필을 서버에 저장한다. */
class UpdateProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(request: ProfileUpdateRequest): AppResult<UserProfile> {
        return settingsRepository.updateProfile(request)
    }
}
