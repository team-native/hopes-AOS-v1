package com.example.hopes.domain.usecase

import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class ConfirmSignupVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 사용자가 입력한 회원가입 인증번호를 서버에 확인받는다.
    // 인증번호 입력 화면의 "확인" 클릭에서 호출되며, 성공 시 회원가입 입력 단계로 이동한다.
    suspend operator fun invoke(email: String, code: String): AppResult<Unit> {
        return authRepository.confirmSignupCode(email, code)
    }
}
