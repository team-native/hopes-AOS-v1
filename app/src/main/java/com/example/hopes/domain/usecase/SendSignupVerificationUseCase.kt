package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class SendSignupVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 회원가입용 이메일 인증번호 발송을 요청한다.
    // 이메일 인증 화면의 "인증번호 받기" 클릭에서 호출되며, 성공 시 인증번호 입력 단계로 이동한다.
    suspend operator fun invoke(email: String): AppResult<Unit> {
        return authRepository.sendSignupCode(email)
    }
}
