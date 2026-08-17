package com.example.hopes.feature.auth.presentation.signupverification

import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.signupverification.content.SignUpCodeConfirmationScreenContent

/** 회원가입 인증번호 입력 화면의 진입점이다. */
@Composable
fun SignUpCodeConfirmationScreen(
    uiState: SignUpCodeConfirmationUiState,
    onEvent: (SignUpCodeConfirmationScreenEvent) -> Unit,
) {
    FigmaPhoneScreen(useFigmaViewport = false) {
        SignUpCodeConfirmationScreenContent(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
