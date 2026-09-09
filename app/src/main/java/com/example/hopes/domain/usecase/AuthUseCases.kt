package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(username: String, password: String): AppResult<AuthToken> {
        return authRepository.login(username, password)
    }
}
