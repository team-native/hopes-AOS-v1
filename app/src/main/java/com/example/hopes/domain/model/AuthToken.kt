package com.example.hopes.domain.model

data class AuthToken(
    val accessToken: String,
    val tokenType: String,
    val message: String? = null,
)
