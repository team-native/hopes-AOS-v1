package com.example.hopes.data.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationCheckContractTest {
    private val json = Json { encodeDefaults = true; explicitNulls = false }

    @Test
    fun `위치 확인 요청은 OAS 필드와 ISO 측정 시간을 전송한다`() {
        val encoded = json.encodeToString(LocationCheckRequestDto(37.4, 127.1, 120.0, "2026-11-24T00:00:00Z", "request-1"))

        assertTrue(encoded.contains("\"horizontalAccuracyMeters\":120.0"))
        assertTrue(encoded.contains("\"measuredAt\":\"2026-11-24T00:00:00Z\""))
        assertTrue(encoded.contains("\"clientRequestId\":\"request-1\""))
    }

    @Test
    fun `위치 확인 응답의 서버 건물 ID는 이름으로 변환하지 않고 보존한다`() {
        val parsed = json.decodeFromString<LocationCheckResponseDto>("""{"status":"NEAR","accuracyMeters":15.5,"message":"위치를 확인했어요","floorPlansAvailable":true,"matchedBuilding":42}""")

        assertEquals(42L, parsed.matchedBuilding)
        assertEquals("NEAR", parsed.status)
    }
}
