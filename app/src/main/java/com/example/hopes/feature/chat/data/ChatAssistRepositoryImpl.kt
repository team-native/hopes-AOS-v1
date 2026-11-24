package com.example.hopes.feature.chat.data

import com.example.hopes.data.api.ChatPreferenceUpdateDto
import com.example.hopes.data.api.RecommendationConsentRequestDto
import com.example.hopes.data.mapper.toAppResult
import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreference
import com.example.hopes.domain.model.ChatPreferenceUpdate
import com.example.hopes.domain.model.RecommendationConsent
import com.example.hopes.domain.model.RecommendationPage
import com.example.hopes.domain.repository.ChatAssistRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class ChatAssistRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChatAssistRemoteDataSource,
) : ChatAssistRepository {
    override suspend fun getDepartments(): AppResult<Map<String, List<ChatAssistCategory>>> =
        remoteDataSource.departments().toAppResult { it.toDomainCategories() }

    override suspend fun getSpecialties(): AppResult<Map<String, List<ChatAssistCategory>>> =
        remoteDataSource.specialties().toAppResult { it.toDomainCategories() }

    override suspend fun getPreferences(): AppResult<ChatPreference> =
        remoteDataSource.preferences().toAppResult { it.toDomain() }

    override suspend fun updatePreferences(update: ChatPreferenceUpdate): AppResult<ChatPreference> =
        remoteDataSource.updatePreferences(
            ChatPreferenceUpdateDto(update.departmentId, update.specialtyId, update.specialtyIds),
        ).toAppResult { it.toDomain() }

    override suspend fun getRecommendationConsent(): AppResult<RecommendationConsent> =
        remoteDataSource.consent().toAppResult { it.toDomain() }

    override suspend fun updateRecommendationConsent(enabled: Boolean, policyVersion: String): AppResult<RecommendationConsent> =
        remoteDataSource.updateConsent(RecommendationConsentRequestDto(enabled, policyVersion)).toAppResult { it.toDomain() }

    override suspend fun getRecommendations(
        departmentId: Long?,
        specialtyId: Long?,
        refreshToken: String?,
        excludeIds: List<Long>,
    ): AppResult<RecommendationPage> =
        remoteDataSource.recommendations(departmentId, specialtyId, refreshToken, excludeIds).toAppResult { it.toDomain() }
}
