package com.example.hopes.feature.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.feature.auth.presentation.onboarding.OnboardingScreen
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
    onNavigateGuide: () -> Unit,
    onOnboardingStart: () -> Unit,
    passwordResetUiState: PasswordResetUiState,
    onPasswordResetEvent: (PasswordResetScreenEvent) -> Unit,
    signupContent: @Composable () -> Unit,
) {
    when (authStep) {
        // 로그인은 따로 화면을 바꾸지 않고 시작 화면 위로 바텀시트를 올린다.
        AuthStep.Guide, AuthStep.Login -> {
            Box(modifier = Modifier.fillMaxSize()) {
                AuthStartScreen(
                    onStartClick = onNavigateSignup,
                    onLoginClick = onNavigateLogin,
                )

                AnimatedVisibility(
                    visible = authStep == AuthStep.Login,
                    // 시작 화면은 그대로 두고 시트만 아래에서 올라오고 내려간다.
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    AuthLoginSheet(
                        uiState = loginUiState,
                        onEvent = onLoginEvent,
                        onDismiss = onNavigateGuide,
                    )
                }
            }
        }

        AuthStep.Onboarding -> {
            OnboardingScreen(onStartClick = onOnboardingStart)
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
