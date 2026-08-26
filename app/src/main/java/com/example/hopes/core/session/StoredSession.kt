package com.example.hopes.core.session

/** DataStore에서 복원한 세션 인증 정보다. */
data class StoredSession(
    val accessToken: String,
    val tokenType: String,
)
