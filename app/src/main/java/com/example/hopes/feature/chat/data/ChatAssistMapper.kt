package com.example.hopes.feature.chat.data

import com.example.hopes.data.api.ChatCategoryDto
import com.example.hopes.data.api.ChatPreferenceDto
import com.example.hopes.data.api.LocationCheckResponseDto
import com.example.hopes.data.api.QuestionRecommendationResultDto
import com.example.hopes.data.api.RecommendationConsentDto
import com.example.hopes.domain.model.CampusLocationCheckResult
import com.example.hopes.domain.model.ChatAssistCategory
import com.example.hopes.domain.model.ChatPreference
import com.example.hopes.domain.model.RecommendationConsent
import com.example.hopes.domain.model.RecommendationPage
import com.example.hopes.domain.model.RecommendedQuestion

internal fun ChatCategoryDto.toDomain(): ChatAssistCategory =
    ChatAssistCategory(id, name, description, sortOrder, isActive)

internal fun Map<String, List<ChatCategoryDto>>.toDomainCategories(): Map<String, List<ChatAssistCategory>> =
    mapValues { (_, categories) -> categories.map(ChatCategoryDto::toDomain) }

internal fun ChatPreferenceDto.toDomain(): ChatPreference =
    ChatPreference(departmentId, specialtyId, invalidSelection, specialtyIds)

internal fun RecommendationConsentDto.toDomain(): RecommendationConsent =
    RecommendationConsent(enabled, policyVersion, updatedAt)

internal fun QuestionRecommendationResultDto.toDomain(): RecommendationPage =
    RecommendationPage(
        items = items.map { item -> RecommendedQuestion(item.id, item.content, item.departmentId, item.specialtyId) },
        source = source,
        fallbackUsed = fallbackUsed,
        nextRefreshToken = nextRefreshToken,
    )

internal fun LocationCheckResponseDto.toDomain(): CampusLocationCheckResult =
    CampusLocationCheckResult(status, accuracyMeters, message, floorPlansAvailable, matchedBuilding)
