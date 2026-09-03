package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.core.designsystem.component.overlay.overlayBackdropBlur
import com.example.hopes.feature.auth.presentation.component.AuthLoginBackdrop
import com.example.hopes.feature.auth.presentation.content.AuthLoginSheetContent

/** 피그마 02 로그인 화면 진입점이다. */
@Composable
fun AuthLoginScreen(
    emailText: String,
    passwordText: String,
    loginErrorMessage: String?,
    loginStatusMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onDismissLogin: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    FigmaPhoneScreen(
        applyStatusBarsPadding = true,
        background = {
            AuthLoginBackdrop(
                modifier = Modifier
                    .fillMaxSize()
                    .overlayBackdropBlur(),
            )
        },
    ) {
        AuthLoginSheetContent(
            emailText = emailText,
            passwordText = passwordText,
            loginErrorMessage = loginErrorMessage,
            loginStatusMessage = loginStatusMessage,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onNavigateSignup = onNavigateSignup,
            onDismissLogin = onDismissLogin,
            onForgotPasswordClick = onForgotPasswordClick,
        )
    }
}
