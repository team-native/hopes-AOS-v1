package com.example.hopes.feature.auth.presentation.signup.viewmodel

import com.example.hopes.core.validation.canStillBecomeSchoolEmail
import com.example.hopes.core.validation.containsLetterAndDigit
import com.example.hopes.core.validation.containsSpecialCharacter
import com.example.hopes.core.validation.hasSignupPasswordMinimumLength
import com.example.hopes.core.validation.isValidSchoolEmail
import com.example.hopes.core.validation.isValidSignupPassword
import com.example.hopes.core.validation.isValidUsername

/** 회원가입 3단계(이메일 인증 → 비밀번호 → 프로필)의 입력값과 진행 상태다. */
data class SignupUiState(
    val step: SignupStep = SignupStep.Email,
    val email: String = "",
    val isCodeSent: Boolean = false,
    val code: String = "",
    val codeError: SignupCodeError? = null,
    val failedCodeAttemptCount: Int = 0,
    val remainingSeconds: Int = 0,
    val isEmailVerified: Boolean = false,
    val password: String = "",
    val passwordConfirm: String = "",
    val name: String = "",
    val department: SignupDepartment? = null,
    val generation: Int? = null,
    val isGenerationErrorVisible: Boolean = false,
    val isTermsAgreed: Boolean = false,
    val isLoading: Boolean = false,
    val requestError: SignupRequestError? = null,
) {
    /** 다른 도메인이 확정된 순간부터 학교 이메일 오류를 표시한다. */
    val isEmailDomainErrorVisible: Boolean
        get() = email.isNotEmpty() && !email.canStillBecomeSchoolEmail()

    val canRequestCode: Boolean
        get() = email.isValidSchoolEmail() && !isLoading

    /** 만료·횟수 초과 상태에서는 확인 대신 인증번호 재발송만 할 수 있다. */
    val isCodeLocked: Boolean
        get() = codeError == SignupCodeError.Expired || codeError == SignupCodeError.AttemptsExceeded

    val canConfirmCode: Boolean
        get() = code.length == SIGNUP_VERIFICATION_CODE_LENGTH && !isCodeLocked && !isLoading

    val passwordRules: SignupPasswordRules
        get() =
            SignupPasswordRules(
                hasMinimumLength = password.hasSignupPasswordMinimumLength(),
                hasLetterAndDigit = password.containsLetterAndDigit(),
                hasSpecialCharacter = password.containsSpecialCharacter(),
            )

    /** 확인란 입력을 시작했는데 비밀번호가 조건에 맞지 않으면 오류로 표시한다. */
    val isPasswordRuleErrorVisible: Boolean
        get() = password.isNotEmpty() && passwordConfirm.isNotEmpty() && !password.isValidSignupPassword()

    val isPasswordMismatchVisible: Boolean
        get() = passwordConfirm.isNotEmpty() && password != passwordConfirm

    val canSubmitPassword: Boolean
        get() = password.isValidSignupPassword() && password == passwordConfirm

    val canCompleteSignup: Boolean
        get() =
            name.isValidUsername() &&
                department != null &&
                generation != null &&
                isTermsAgreed &&
                !isLoading
}

const val SIGNUP_VERIFICATION_CODE_LENGTH = 6
const val SIGNUP_MAX_CODE_ATTEMPT_COUNT = 5

/** 인증번호 유효 시간이다. 서버가 만료 시각을 내려주지 않아 앱에서 5분을 센다. */
const val SIGNUP_CODE_VALID_SECONDS = 300

val SIGNUP_GENERATION_OPTIONS: List<Int> = listOf(8, 9, 10, 11)
