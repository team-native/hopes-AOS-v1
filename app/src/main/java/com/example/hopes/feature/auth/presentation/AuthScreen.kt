package com.example.hopes.feature.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
            // 시트 높이는 시트가 알려 준다. 처음 한 번은 측정 전이라 대략의 높이로 시작 화면을 먼저 올려 둔다.
            var loginSheetHeight by remember { mutableStateOf<Dp?>(null) }
            Box(modifier = Modifier.fillMaxSize()) {
                AuthStartScreen(
                    onStartClick = onNavigateSignup,
                    onLoginClick = onNavigateLogin,
                    loginSheetHeight =
                        if (authStep == AuthStep.Login) {
                            loginSheetHeight ?: ESTIMATED_LOGIN_SHEET_HEIGHT
                        } else {
                            null
                        },
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
                        onSheetHeightChange = { height -> loginSheetHeight = height },
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

// 시트 높이를 처음 측정하기 전까지 시작 화면을 올려 둘 때 쓰는 대략의 높이다.
private val ESTIMATED_LOGIN_SHEET_HEIGHT = 420.dp
