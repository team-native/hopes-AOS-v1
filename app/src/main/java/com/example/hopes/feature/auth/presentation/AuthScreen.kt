package com.example.hopes.feature.auth.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreen
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState

/**
 * 인증 단계에 맞는 Screen을 고른다.
 *
 * @param signupContent 회원가입은 자체 ViewModel을 가진 별도 Route라서, AuthRoute가 그 Route를 slot으로 넘긴다.
 */
@Composable
fun AuthScreen(
    authStep: AuthStep,
    loginUiState: AuthLoginUiState,
    onLoginEvent: (AuthLoginScreenEvent) -> Unit,
    onNavigateSignup: () -> Unit,
    onNavigateLogin: () -> Unit,
    passwordResetUiState: PasswordResetUiState,
    onPasswordResetEvent: (PasswordResetScreenEvent) -> Unit,
    signupContent: @Composable () -> Unit,
) {
    when (authStep) {
        AuthStep.Guide -> {
            AuthStartScreen(
                onStartClick = onNavigateSignup,
                onLoginClick = onNavigateLogin,
            )
        }

        AuthStep.Login -> {
            AuthLoginScreen(
                uiState = loginUiState,
                onEvent = onLoginEvent,
            )
        }

        AuthStep.PasswordResetRequest -> {
            PasswordResetScreen(
                uiState = passwordResetUiState,
                onEvent = onPasswordResetEvent,
            )
        }

        AuthStep.SignUp -> {
            signupContent()
        }
    }
}
