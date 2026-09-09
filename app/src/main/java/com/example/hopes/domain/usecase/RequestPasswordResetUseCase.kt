package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class RequestPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 학교 이메일로 비밀번호 재설정 인증번호 발송을 요청한다.
    // 비밀번호 재설정 화면의 "인증번호 받기" 클릭에서 호출되며, 성공 시 서버가 이메일로 인증번호를 보낸다.
    suspend operator fun invoke(email: String): AppResult<String> {
        return authRepository.requestPasswordReset(email)
    }
}
