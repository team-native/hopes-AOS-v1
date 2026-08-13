package com.example.hopes.domain.repository

import com.example.hopes.domain.model.AppTheme
import com.example.hopes.domain.model.ProfileUpdate
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.result.AppResult

interface SettingsRepository {
    suspend fun logout(): AppResult<Unit>

    suspend fun updateTheme(theme: AppTheme): AppResult<AppTheme>

    suspend fun getProfile(): AppResult<UserProfile>

    suspend fun updateProfile(update: ProfileUpdate): AppResult<UserProfile>

    suspend fun getSettings(): AppResult<UserSettings>

    suspend fun updateCustomPrompt(customPrompt: String): AppResult<UserSettings>

    suspend fun submitInquiry(content: String): AppResult<Unit>
}
