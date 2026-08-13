package com.example.hopes.data.mapper

import com.example.hopes.data.api.TokenResponseDto
import com.example.hopes.domain.model.AuthToken

fun TokenResponseDto.toDomain(): AuthToken {
    return AuthToken(
        accessToken = accessToken,
        tokenType = tokenType,
    )
}
