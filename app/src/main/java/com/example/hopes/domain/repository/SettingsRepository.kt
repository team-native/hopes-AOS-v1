package com.example.hopes.domain.repository

import com.example.hopes.domain.model.ProfileUpdateRequest
import com.example.hopes.domain.model.UserProfile
import com.example.hopes.domain.model.UserSettings
import com.example.hopes.domain.result.AppResult

interface SettingsRepository {
    suspend fun logout(): AppResult<Unit>
    suspend fun deleteAccount(password: String): AppResult<Unit>
    suspend fun updateTheme(theme: String): AppResult<String>
    suspend fun getProfile(): AppResult<UserProfile>
    suspend fun updateProfile(request: ProfileUpdateRequest): AppResult<UserProfile>
    suspend fun getSettings(): AppResult<UserSettings>
    suspend fun updateCustomPrompt(customPrompt: String?): AppResult<UserSettings>
    suspend fun submitInquiry(content: String): AppResult<Unit>
}
