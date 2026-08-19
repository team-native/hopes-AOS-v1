package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.component.AuthBackground
import com.example.hopes.feature.auth.presentation.content.AuthLoginSheetContent

/** 피그마 02 로그인 화면 진입점이다. */
@Composable
fun AuthLoginScreen(
    emailText: String,
    passwordText: String,
    loginErrorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onDismissLogin: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    FigmaPhoneScreen(
        applyStatusBarsPadding = true,
        navigationBarColor = MaterialTheme.colorScheme.surface,
        background = {
            // 피그마의 배경은 선명한 그라디언트이고, 전경 브랜드/카피에만 8px 블러가 적용된다.
            AuthBackground(modifier = Modifier.fillMaxSize())
        },
    ) {
        AuthLoginSheetContent(
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
    }
}
