package com.example.hopes.domain.model

data class ChatAssistCategory(
    val id: Long,
    val name: String,
    val description: String,
    val sortOrder: Int,
    val isActive: Boolean,
)

data class ChatPreference(
    val departmentId: Long?,
    val specialtyId: Long?,
    val invalidSelection: Boolean,
    val specialtyIds: List<Long>,
)

data class ChatPreferenceUpdate(
    val departmentId: Long?,
    val specialtyId: Long?,
    val specialtyIds: List<Long>,
)

data class RecommendationConsent(
    val enabled: Boolean,
    val policyVersion: String,
    val updatedAt: String?,
)

data class RecommendedQuestion(
    val id: Long,
    val content: String,
    val departmentId: Long?,
    val specialtyId: Long?,
)

data class RecommendationPage(
    val items: List<RecommendedQuestion>,
    val source: String,
    val fallbackUsed: Boolean,
    val nextRefreshToken: String,
)
