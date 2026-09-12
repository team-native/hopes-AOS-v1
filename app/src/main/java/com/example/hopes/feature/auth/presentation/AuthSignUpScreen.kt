package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.content.AuthSignUpScreenContent

/** 피그마 03 회원가입 화면 진입점이다. */
@Composable
fun AuthSignUpScreen(
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCodeText: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    // 회원가입은 인증 흐름의 독립 화면이므로 하단 탭을 표시하지 않는다.
    // 파랑-하얀 배경은 스크롤되는 페이지 아이템 안에서 함께 이동한다.
    FigmaPhoneScreen {
        AuthSignUpScreenContent(
            emailText = emailText,
            passwordText = passwordText,
            nameText = nameText,
            departmentText = departmentText,
            generationText = generationText,
            verificationCodeText = verificationCodeText,
            signupValidation = signupValidation,
            isLoading = isLoading,
            errorMessage = errorMessage,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNameChange = onNameChange,
            onDepartmentClick = onDepartmentClick,
            onGenerationClick = onGenerationClick,
            onVerificationCodeChange = onVerificationCodeChange,
            onSendVerificationCodeClick = onSendVerificationCodeClick,
            onActionClick = onActionClick,
            onFooterClick = onFooterClick,
        )
    }
}
