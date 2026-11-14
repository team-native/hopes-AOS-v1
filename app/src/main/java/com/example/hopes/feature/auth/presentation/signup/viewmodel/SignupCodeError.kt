package com.example.hopes.feature.auth.presentation.signup.viewmodel

/** 인증번호 입력란 아래에 표시하는 오류다. */
sealed interface SignupCodeError {
    data class Mismatch(
        val failedAttemptCount: Int,
    ) : SignupCodeError

    data object AttemptsExceeded : SignupCodeError

    data object Expired : SignupCodeError
}
