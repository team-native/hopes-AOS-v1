package com.example.hopes.domain.usecase

import com.example.hopes.domain.model.PasswordResetRequest
import com.example.hopes.domain.repository.AuthRepository
import com.example.hopes.domain.result.AppResult
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    // 인증번호와 새 비밀번호를 서버에 제출해 비밀번호 재설정을 완료한다.
    // 비밀번호 재설정 화면의 "완료" 클릭에서 호출된다.
    suspend operator fun invoke(
        email: String,
        code: String,
        password: String,
        passwordConfirm: String,
    ): AppResult<Unit> {
        return authRepository.resetPassword(
            PasswordResetRequest(email = email, code = code, password = password, passwordConfirm = passwordConfirm),
        )
    }
}
