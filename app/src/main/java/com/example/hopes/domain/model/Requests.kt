package com.example.hopes.domain.model

data class SignUpRequest(
    val email: String,
    val username: String,
    val password: String,
    val passwordConfirm: String,
    val verificationCode: String,
    val gender: String? = null,
    val major: String? = null,
    val cohort: Int? = null,
)

data class PasswordResetRequest(
    val email: String,
    val code: String,
    val password: String,
    val passwordConfirm: String,
)

data class ProfileUpdateRequest(
    val username: String? = null,
    val nickname: String? = null,
    val profileInfo: String? = null,
    val profileImage: String? = null,
)
