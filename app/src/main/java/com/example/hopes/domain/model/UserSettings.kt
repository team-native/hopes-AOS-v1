package com.example.hopes.domain.model

/** 마이페이지와 설정에서 공유하는 사용자 정보다. */
data class UserProfile(
    val username: String,
    val email: String,
    val nickname: String,
    val profileInfo: String,
    val profileImage: String?,
    val gender: String?,
    val major: String?,
    val cohort: Int?,
)

/** 서버에서 지원하는 앱 테마 값이다. */
enum class AppTheme {
    Light,
    Dark,
    Unknown,
}

/** 설정 메인 화면에 필요한 서버 상태다. */
data class UserSettings(
    val profile: UserProfile,
    val theme: AppTheme,
    val customPrompt: String,
    val canLogout: Boolean,
    val canSubmitInquiry: Boolean,
)

/** 프로필 저장에 전달하는 선택적 변경 필드다. */
data class ProfileUpdate(
    val username: String?,
    val nickname: String?,
    val profileInfo: String?,
    val profileImage: String?,
)
