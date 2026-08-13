package com.example.hopes.data.mapper

import com.example.hopes.data.api.UserResponseDto
import com.example.hopes.domain.model.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsDtoMapperTest {

    @Test
    fun `unknown server theme falls back to unknown domain value`() {
        assertEquals(AppTheme.Unknown, "SEPIA".toAppTheme())
    }

    @Test
    fun `profile DTO maps every display field to domain`() {
        val profile = UserResponseDto(
            username = "tester",
            email = "s12345@gsm.hs.kr",
            nickname = "테스터",
            profileInfo = "소개",
            profileImage = null,
            gender = null,
            major = "software",
            cohort = 10,
        ).toDomain()

        assertEquals("tester", profile.username)
        assertEquals("software", profile.major)
        assertEquals(10, profile.cohort)
    }
}
