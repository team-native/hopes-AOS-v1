package com.example.hopes.feature.auth.presentation.signupverification

import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.signupverification.content.SignUpEmailVerificationScreenContent

/** 회원가입 이메일 인증번호 발송 화면의 진입점이다. */
@Composable
fun SignUpEmailVerificationScreen(
    uiState: SignUpEmailVerificationUiState,
    onEvent: (SignUpEmailVerificationScreenEvent) -> Unit,
) {
    FigmaPhoneScreen(useFigmaViewport = false) {
        SignUpEmailVerificationScreenContent(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
