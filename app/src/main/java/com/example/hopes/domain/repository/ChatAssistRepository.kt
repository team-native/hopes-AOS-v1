package com.example.hopes.domain.repository

import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreference
import com.example.hopes.domain.model.ChatPreferenceUpdate
import com.example.hopes.domain.model.RecommendationConsent
import com.example.hopes.domain.model.RecommendationPage
import com.example.hopes.domain.result.AppResult

interface ChatAssistRepository {
    suspend fun getDepartments(): AppResult<Map<String, List<ChatAssistCategory>>>
    suspend fun getSpecialties(): AppResult<Map<String, List<ChatAssistCategory>>>
    suspend fun getPreferences(): AppResult<ChatPreference>
    suspend fun updatePreferences(update: ChatPreferenceUpdate): AppResult<ChatPreference>
    suspend fun getRecommendationConsent(): AppResult<RecommendationConsent>
    suspend fun updateRecommendationConsent(enabled: Boolean, policyVersion: String): AppResult<RecommendationConsent>
    suspend fun getRecommendations(departmentId: Long?, specialtyId: Long?, refreshToken: String?, excludeIds: List<Long>): AppResult<RecommendationPage>
}
