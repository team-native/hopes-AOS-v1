package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.core.designsystem.component.overlay.overlayBackdropBlur
import com.example.hopes.feature.auth.presentation.component.AuthSharedBackdrop
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
    onForgotPasswordClick: () -> Unit,
    isInitiallyExpanded: Boolean,
) {
    var sheetExpansionProgress by remember(isInitiallyExpanded) {
        mutableFloatStateOf(if (isInitiallyExpanded) 1f else 0f)
    }

    FigmaPhoneScreen(
        applyStatusBarsPadding = true,
        background = {
            AuthSharedBackdrop(
                sheetExpansionProgress = sheetExpansionProgress,
                modifier = Modifier
                    .fillMaxSize()
                    .overlayBackdropBlur(sheetExpansionProgress),
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
            onForgotPasswordClick = onForgotPasswordClick,
            isInitiallyExpanded = isInitiallyExpanded,
            onSheetExpansionProgressChanged = { sheetExpansionProgress = it },
        )
    }
}
