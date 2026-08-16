package com.example.hopes.domain.model

data class UserProfile(
    val username: String,
    val email: String,
    val nickname: String,
    val profileInfo: String,
    val profileImage: String?,
    val major: String? = null,
)

data class UserSettings(
    val profile: UserProfile,
    val theme: String,
    val customPrompt: String,
)
