package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    // 마이페이지 저장 클릭 시 변경한 별명과 자기소개를 서버에 반영한다.
    // 서버 응답 프로필 또는 오류 결과를 호출한 ViewModel에 전달한다.
    suspend operator fun invoke(request: ProfileUpdateRequest): AppResult<UserProfile> {
        return settingsRepository.updateProfile(request)
    }
}
