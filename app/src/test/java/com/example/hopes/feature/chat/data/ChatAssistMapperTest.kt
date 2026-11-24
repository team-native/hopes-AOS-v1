package com.example.hopes.feature.chat.data

import com.example.hopes.data.api.ChatCategoryDto
import com.example.hopes.data.api.LocationCheckResponseDto
import com.example.hopes.data.api.QuestionRecommendationDto
import com.example.hopes.data.api.QuestionRecommendationResultDto
import org.junit.Assert.assertEquals
import org.junit.Test

class ChatAssistMapperTest {
    @Test
    fun `서버 카테고리를 도메인 카테고리로 변환한다`() {
        val categories = mapOf("인공지능" to listOf(ChatCategoryDto(12, "인공지능", "설명", 2, true)))

        assertEquals(12L, categories.toDomainCategories().getValue("인공지능").single().id)
        assertEquals("설명", categories.toDomainCategories().getValue("인공지능").single().description)
    }

    @Test
    fun `추천 응답과 위치 응답을 서버 의미 그대로 도메인에 보존한다`() {
        val recommendation = QuestionRecommendationResultDto(
            items = listOf(QuestionRecommendationDto(4, "질문", departmentId = 12, specialtyId = 20)),
            source = "PERSONALIZED",
            fallbackUsed = false,
            nextRefreshToken = "refresh-2",
        )
        val location = LocationCheckResponseDto("SERVER_STATUS", 37.5, "서버 안내", true, 99)

        assertEquals(20L, recommendation.toDomain().items.single().specialtyId)
        assertEquals("refresh-2", recommendation.toDomain().nextRefreshToken)
        assertEquals("SERVER_STATUS", location.toDomain().status)
        assertEquals(99L, location.toDomain().matchedBuildingId)
    }
}
