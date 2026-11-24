package com.example.hopes.feature.chat.data

import com.example.hopes.core.network.ApiExecutor
import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.ChatAssistApiService
import com.example.hopes.data.api.ChatCategoryDto
import com.example.hopes.data.api.ChatPreferenceDto
import com.example.hopes.data.api.ChatPreferenceUpdateDto
import com.example.hopes.data.api.QuestionRecommendationResultDto
import com.example.hopes.data.api.RecommendationConsentDto
import com.example.hopes.data.api.RecommendationConsentRequestDto
import javax.inject.Inject

/** 학과·전공 선택과 질문 추천 API를 호출하는 remote data source다. */
interface ChatAssistRemoteDataSource {
    suspend fun departments(): NetworkResult<Map<String, List<ChatCategoryDto>>>
    suspend fun specialties(): NetworkResult<Map<String, List<ChatCategoryDto>>>
    suspend fun preferences(): NetworkResult<ChatPreferenceDto>
    suspend fun updatePreferences(body: ChatPreferenceUpdateDto): NetworkResult<ChatPreferenceDto>
    suspend fun consent(): NetworkResult<RecommendationConsentDto>
    suspend fun updateConsent(body: RecommendationConsentRequestDto): NetworkResult<RecommendationConsentDto>
    suspend fun recommendations(departmentId: Long?, specialtyId: Long?, refreshToken: String?, excludeIds: List<Long>): NetworkResult<QuestionRecommendationResultDto>
}

class ChatAssistRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiExecutor: ApiExecutor,
        private val api: ChatAssistApiService,
    ) : ChatAssistRemoteDataSource {
        override suspend fun departments(): NetworkResult<Map<String, List<ChatCategoryDto>>> = apiExecutor.execute { api.getDepartments() }
        override suspend fun specialties(): NetworkResult<Map<String, List<ChatCategoryDto>>> = apiExecutor.execute { api.getSpecialties() }
        override suspend fun preferences(): NetworkResult<ChatPreferenceDto> = apiExecutor.execute { api.getChatPreferences() }
        override suspend fun updatePreferences(body: ChatPreferenceUpdateDto): NetworkResult<ChatPreferenceDto> = apiExecutor.execute { api.updateChatPreferences(body) }
        override suspend fun consent(): NetworkResult<RecommendationConsentDto> = apiExecutor.execute { api.getRecommendationConsent() }
        override suspend fun updateConsent(body: RecommendationConsentRequestDto): NetworkResult<RecommendationConsentDto> = apiExecutor.execute { api.updateRecommendationConsent(body) }
        override suspend fun recommendations(departmentId: Long?, specialtyId: Long?, refreshToken: String?, excludeIds: List<Long>): NetworkResult<QuestionRecommendationResultDto> =
            apiExecutor.execute { api.getRecommendations(departmentId, specialtyId, 4, refreshToken, excludeIds, departmentId != null && specialtyId != null, specialtyId?.let(::listOf)) }
    }
