package com.example.hopes.domain.repository

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.result.AppResult

interface AuthRepository {
    suspend fun login(username: String, password: String): AppResult<AuthToken>
    suspend fun signUp(request: SignUpRequest): AppResult<AuthToken>
    suspend fun sendSignupCode(email: String): AppResult<Unit>
    suspend fun requestPasswordReset(email: String): AppResult<Unit>
    suspend fun resetPassword(request: PasswordResetRequest): AppResult<Unit>
}
