package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.ProfileUpdate
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.repository.SettingsRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** 마이페이지 저장 이벤트에서 명세 길이 제한을 검증하고 변경 정보를 저장한다. */
    suspend operator fun invoke(update: ProfileUpdate): AppResult<UserProfile> {
        if ((update.username?.length ?: 0) > MAX_NAME_LENGTH ||
            (update.nickname?.length ?: 0) > MAX_NAME_LENGTH ||
            (update.profileInfo?.length ?: 0) > MAX_PROFILE_INFO_LENGTH ||
            (update.profileImage?.length ?: 0) > MAX_IMAGE_URL_LENGTH
        ) {
            return AppResult.Failure(AppError.Validation)
        }

        return settingsRepository.updateProfile(update)
    }

    private companion object {
        const val MAX_NAME_LENGTH = 50
        const val MAX_PROFILE_INFO_LENGTH = 2_000
        const val MAX_IMAGE_URL_LENGTH = 255
    }
}
