package com.example.hopes.feature.auth.presentation.passwordreset

import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.passwordreset.content.PasswordResetScreenContent

/** 피그마 13 비밀번호 재설정 이메일 입력 화면의 진입점이다. */
@Composable
fun PasswordResetScreen(
    uiState: PasswordResetUiState,
    onEvent: (PasswordResetScreenEvent) -> Unit,
) {
    FigmaPhoneScreen(useFigmaViewport = false) {
        PasswordResetScreenContent(
            uiState = uiState,
            onEvent = onEvent,
        )
    }
}
