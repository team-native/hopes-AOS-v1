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
    val passwordResetEmail: String = "",
    val passwordResetCode: String = "",
    val passwordResetNewPassword: String = "",
    val isPasswordResetCodeSent: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
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
