package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class ConfirmSignupVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 사용자가 입력한 회원가입 인증번호를 서버에 확인받는다.
    // "회원가입" 클릭 시 최종 가입 요청을 보내기 전 먼저 호출되며, 성공했을 때만 나머지 입력값을 전송한다.
    suspend operator fun invoke(email: String, code: String): AppResult<Unit> {
        return authRepository.confirmSignupCode(email, code)
    }
}
