package com.example.hopes.data.mapper

import com.example.hopes.data.api.SettingMainResponseDto
import com.example.hopes.data.api.UserResponseDto
import com.example.hopes.domain.model.AppTheme
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings

fun UserResponseDto.toDomain(): UserProfile {
    return UserProfile(
        username = username,
        email = email,
        nickname = nickname,
        profileInfo = profileInfo,
        profileImage = profileImage,
        gender = gender,
        major = major,
        cohort = cohort,
    )
}

fun SettingMainResponseDto.toDomain(): UserSettings {
    return UserSettings(
        profile = accountSetting.toDomain(),
        theme = theme.toAppTheme(),
        customPrompt = customPrompt,
        canLogout = logout,
        canSubmitInquiry = inquiry,
    )
}

fun String.toAppTheme(): AppTheme {
    return when (this) {
        "LIGHT" -> AppTheme.Light
        "DARK" -> AppTheme.Dark
        else -> AppTheme.Unknown
    }
}

fun AppTheme.toApiValue(): String {
    return when (this) {
        AppTheme.Light -> "LIGHT"
        AppTheme.Dark -> "DARK"
        AppTheme.Unknown -> "LIGHT"
    }
}
