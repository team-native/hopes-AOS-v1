package com.example.hopes.domain.model

/** 로그인 성공 후 인증 요청에 사용하는 액세스 토큰이다. */
data class AuthToken(
    val accessToken: String,
    val tokenType: String,
)
