package com.example.hopes.feature.chat.data

import com.example.hopes.core.network.NetworkResult
import com.example.hopes.data.api.ChatCategoryDto
import com.example.hopes.data.api.ChatPreferenceDto
import com.example.hopes.data.api.ChatPreferenceUpdateDto
import com.example.hopes.data.api.LocationCheckRequestDto
import com.example.hopes.data.api.LocationCheckResponseDto
import com.example.hopes.data.api.QuestionRecommendationResultDto
import com.example.hopes.data.api.RecommendationConsentDto
import com.example.hopes.data.api.RecommendationConsentRequestDto
import com.example.hopes.domain.model.CampusLocationCheckRequest
import com.example.hopes.domain.repository.CampusLocationRepository
import com.example.hopes.domain.result.AppResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ChatAssistRepositoryImplTest {
    @Test
    fun `추천 동의 요청을 DTO로 변환하고 응답을 도메인으로 반환한다`() = runBlocking {
        val remote = FakeChatAssistRemoteDataSource()
        val repository = ChatAssistRepositoryImpl(remote)

        val result = repository.updateRecommendationConsent(enabled = true, policyVersion = "v1")

        assertEquals(RecommendationConsentRequestDto(true, "v1"), remote.lastConsentRequest)
        assertTrue(result is AppResult.Success)
        assertEquals("v2", (result as AppResult.Success).value.policyVersion)
    }

    @Test
    fun `위치 요청 필드와 서버 상태를 도메인 경계에 그대로 보존한다`() = runBlocking {
        val remote = FakeCampusLocationRemoteDataSource()
        val repository: CampusLocationRepository = CampusLocationRepositoryImpl(remote)
        val request = CampusLocationCheckRequest(37.4, 127.1, 25.0, "2026-11-24T00:00:00Z", "request-1")

        val result = repository.checkLocation(request)

        assertEquals(
            LocationCheckRequestDto(37.4, 127.1, 25.0, "2026-11-24T00:00:00Z", "request-1"),
            remote.lastRequest,
        )
        assertEquals("UNKNOWN_SERVER_STATUS", (result as AppResult.Success).value.status)
    }

    private class FakeChatAssistRemoteDataSource : ChatAssistRemoteDataSource {
        var lastConsentRequest: RecommendationConsentRequestDto? = null
        override suspend fun departments() = NetworkResult.Success(mapOf("학과" to listOf(ChatCategoryDto(1, "학과", "", 0, true))))
        override suspend fun specialties() = NetworkResult.Success(emptyMap<String, List<ChatCategoryDto>>())
        override suspend fun preferences() = NetworkResult.Success(ChatPreferenceDto(null, null, false, emptyList()))
        override suspend fun updatePreferences(body: ChatPreferenceUpdateDto) = NetworkResult.Success(ChatPreferenceDto(body.departmentId, body.specialtyId, false, body.specialtyIds))
        override suspend fun consent() = NetworkResult.Success(RecommendationConsentDto(false, "v1"))
        override suspend fun updateConsent(body: RecommendationConsentRequestDto): NetworkResult<RecommendationConsentDto> {
            lastConsentRequest = body
            return NetworkResult.Success(RecommendationConsentDto(body.enabled, "v2"))
        }
        override suspend fun recommendations(departmentId: Long?, specialtyId: Long?, refreshToken: String?, excludeIds: List<Long>) =
            NetworkResult.Success(QuestionRecommendationResultDto(emptyList(), "DEFAULT", false, "next"))
    }

    private class FakeCampusLocationRemoteDataSource : CampusLocationRemoteDataSource {
        var lastRequest: LocationCheckRequestDto? = null
        override suspend fun check(request: LocationCheckRequestDto): NetworkResult<LocationCheckResponseDto> {
            lastRequest = request
            return NetworkResult.Success(LocationCheckResponseDto("UNKNOWN_SERVER_STATUS", 25.0, "server message", false, null))
        }
    }
}
