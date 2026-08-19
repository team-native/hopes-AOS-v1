package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreen
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState

/** 피그마 01~04 인증 화면을 단계에 맞는 Screen으로 라우팅한다. */
@Composable
fun AuthScreen(
    authStep: AuthStep,
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCodeText: String,
    signupValidation: SignupValidationUiState,
    isSignupLoading: Boolean,
    signupErrorMessage: String?,
    loginErrorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onNavigateLogin: () -> Unit,
    onDismissLogin: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    passwordResetUiState: PasswordResetUiState,
    onPasswordResetEvent: (PasswordResetScreenEvent) -> Unit,
) {
    when (authStep) {
        AuthStep.Guide -> AuthGuideScreen(onNavigateLogin = onNavigateLogin)
        AuthStep.Login -> AuthLoginScreen(
            emailText = emailText,
            passwordText = passwordText,
            loginErrorMessage = loginErrorMessage,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onNavigateSignup = onNavigateSignup,
            onDismissLogin = onDismissLogin,
            onForgotPasswordClick = onForgotPasswordClick,
        )
        AuthStep.PasswordResetRequest -> PasswordResetScreen(
            uiState = passwordResetUiState,
            onEvent = onPasswordResetEvent,
        )
        AuthStep.SignUp -> AuthSignUpScreen(
            emailText = emailText,
            passwordText = passwordText,
            nameText = nameText,
            departmentText = departmentText,
            generationText = generationText,
            verificationCodeText = verificationCodeText,
            signupValidation = signupValidation,
            isLoading = isSignupLoading,
            errorMessage = signupErrorMessage,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNameChange = onNameChange,
            onDepartmentClick = onDepartmentClick,
            onGenerationClick = onGenerationClick,
            onVerificationCodeChange = onVerificationCodeChange,
            onSendVerificationCodeClick = onSendVerificationCodeClick,
            onActionClick = onSignupClick,
            onFooterClick = onNavigateLogin,
        )
    }
}
