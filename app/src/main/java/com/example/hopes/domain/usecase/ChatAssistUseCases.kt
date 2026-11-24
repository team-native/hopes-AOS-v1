package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreference
import com.example.hopes.domain.model.ChatPreferenceUpdate
import com.example.hopes.domain.model.RecommendationConsent
import com.example.hopes.domain.model.RecommendationPage
import com.example.hopes.domain.repository.ChatAssistRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class GetChatDepartmentsUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(): AppResult<Map<String, List<ChatAssistCategory>>> = repository.getDepartments()
}

class GetChatSpecialtiesUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(): AppResult<Map<String, List<ChatAssistCategory>>> = repository.getSpecialties()
}

class GetChatPreferencesUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(): AppResult<ChatPreference> = repository.getPreferences()
}

class UpdateChatPreferencesUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(update: ChatPreferenceUpdate): AppResult<ChatPreference> = repository.updatePreferences(update)
}

class GetRecommendationConsentUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(): AppResult<RecommendationConsent> = repository.getRecommendationConsent()
}

class UpdateRecommendationConsentUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(enabled: Boolean, policyVersion: String): AppResult<RecommendationConsent> =
        repository.updateRecommendationConsent(enabled, policyVersion)
}

class GetRecommendationsUseCase @Inject constructor(private val repository: ChatAssistRepository) {
    suspend operator fun invoke(
        departmentId: Long?,
        specialtyId: Long?,
        refreshToken: String?,
        excludeIds: List<Long>,
    ): AppResult<RecommendationPage> = repository.getRecommendations(departmentId, specialtyId, refreshToken, excludeIds)
}
