package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppError
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    /** 로그인 버튼 클릭 시 빈 자격 증명을 막고 서버 인증을 요청한다. */
    suspend operator fun invoke(username: String, password: String): AppResult<AuthToken> {
        if (username.isBlank() || password.isBlank()) {
            return AppResult.Failure(AppError.Validation)
        }

        return authRepository.login(username, password)
    }
}
