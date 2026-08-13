package com.example.hopes.feature.auth.presentation

data class AuthUiState(
    val authStep: AuthStep = AuthStep.Guide,
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val department: String = "",
    val generation: String = "",
    val verificationCode: String = "",
    val signupValidation: SignupValidationUiState = SignupValidationUiState(),
    val isLoading: Boolean = false,
    val requestError: AuthRequestError? = null,
)

/** 회원가입 입력값의 수정 여부와 화면에 표시할 형식 오류를 보관한다. */
data class SignupValidationUiState(
    val isEmailTouched: Boolean = false,
    val isNameTouched: Boolean = false,
    val isGenerationTouched: Boolean = false,
    val isPasswordTouched: Boolean = false,
    val isVerificationCodeTouched: Boolean = false,
    val emailError: SignupInputError? = null,
    val nameError: SignupInputError? = null,
    val generationError: SignupInputError? = null,
    val passwordError: SignupInputError? = null,
    val verificationCodeError: SignupInputError? = null,
) {
    val hasError: Boolean
        get() = emailError != null ||
            nameError != null ||
            generationError != null ||
            passwordError != null ||
            verificationCodeError != null
}

enum class SignupInputError {
    InvalidSchoolEmail,
    InvalidUsername,
    GenerationRequired,
    InvalidPassword,
    InvalidVerificationCode,
}

/** 인증 요청 실패를 화면 문구와 분리해 표현한다. */
enum class AuthRequestError {
    SendVerificationCodeFailed,
    SignUpFailed,
}
