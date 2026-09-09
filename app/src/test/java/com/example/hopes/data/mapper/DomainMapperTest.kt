package com.example.hopes.data.mapper

import com.example.hopes.data.api.MessageDto
import com.example.hopes.domain.model.ChatMessageRole
import org.junit.Assert.assertEquals
import org.junit.Test

class DomainMapperTest {
    @Test
    fun messageDto_userRole_mapsToUser() {
        val messageDto = MessageDto(
            id = 1L,
            role = "USER",
            content = "질문",
            createdAt = null,
        )

        val chatMessage = messageDto.toDomain()

        assertEquals(ChatMessageRole.User, chatMessage.role)
    }

    @Test
    fun messageDto_unknownRole_mapsToUnknown() {
        val messageDto = MessageDto(
            id = 2L,
            role = "SYSTEM",
            content = "안내",
            createdAt = null,
        )

        val chatMessage = messageDto.toDomain()

        assertEquals(ChatMessageRole.Unknown, chatMessage.role)
    }
}
