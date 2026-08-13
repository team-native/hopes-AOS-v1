package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.AuthToken
import com.example.hopes.domain.model.SignUpRequest
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

/** 회원가입 이메일로 인증번호 발송을 요청한다. */
class SendSignupCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String): AppResult<Unit> {
        return authRepository.sendSignupCode(email)
    }
}

/** 인증이 완료된 회원가입 정보를 서버에 등록한다. */
class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: SignUpRequest): AppResult<AuthToken> {
        return authRepository.signUp(request)
    }
}
