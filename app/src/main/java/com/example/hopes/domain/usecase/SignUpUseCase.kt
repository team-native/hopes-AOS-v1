package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.SignUpRequest
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 이메일 인증이 끝난 뒤 최종 회원가입 정보를 서버로 제출한다.
    // 회원가입 화면의 제출 클릭에서 호출되며, 성공 시 로그인과 동일하게 access token을 발급받는다.
    suspend operator fun invoke(request: SignUpRequest): AppResult<AuthToken> {
        return authRepository.signUp(request)
    }
}
