package com.example.hopes.feature.auth.presentation.passwordreset.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.component.AuthStatusText
import com.example.hopes.feature.auth.presentation.component.AuthStepHeader
import com.example.hopes.feature.auth.presentation.component.AuthVerificationCodeField
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState

/** 피그마 13 비밀번호 재설정 화면의 헤더, 이메일+인증번호 입력, 새 비밀번호 입력, 제출 버튼을 조합한다. */
@Composable
fun PasswordResetScreenContent(
    uiState: PasswordResetUiState,
    onEvent: (PasswordResetScreenEvent) -> Unit,
) {
    val density = LocalDensity.current
    // 키보드가 열려 있을 때만 완료 버튼과 키보드 사이에 여백을 추가로 준다 (AuthSignUpScreenContent와 동일한 판별 방식).
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
            .padding(
                start = AppSpacing.ScreenHorizontal,
                end = AppSpacing.ScreenHorizontal,
                bottom = if (isImeVisible) AppSpacing.Item else 0.dp,
            ),
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        AuthStepHeader(
            title = stringResource(R.string.password_reset_title),
            subtitle = stringResource(R.string.password_reset_subtitle),
            onBackClick = { onEvent(PasswordResetScreenEvent.BackClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.auth_email)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.email,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.EmailChanged(value)) },
            labelRes = R.string.auth_email,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthFieldLabel(labelRes = R.string.verification_code)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        // 인증번호 입력과 발송 요청을 같은 행에서 함께 받는다 (회원가입 화면과 동일한 컴포넌트를 공유한다).
        AuthVerificationCodeField(
            value = uiState.code,
            isSending = uiState.isLoading,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.CodeChanged(value)) },
            onSendClick = { onEvent(PasswordResetScreenEvent.RequestCodeClicked) },
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.errorMessage != null || uiState.statusMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            // 인증번호 발송 성공 후 재설정 제출이 실패하는 경우도 있으므로, 에러가 있으면 항상 에러 문구를 우선 표시한다.
            AuthStatusText(
                message = if (uiState.errorMessage != null) {
                    stringResource(R.string.generic_error_message)
                } else {
                    stringResource(R.string.verification_sent_message)
                },
                isError = uiState.errorMessage != null,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.newPassword,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.NewPasswordChanged(value)) },
            labelRes = R.string.password,
            isPassword = true,
            onImeAction = { onEvent(PasswordResetScreenEvent.SubmitClicked) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthPrimaryActionButton(
            text = stringResource(R.string.password_reset_complete),
            isEnabled = uiState.isCodeSent &&
                uiState.code.isNotBlank() &&
                uiState.newPassword.isNotBlank() &&
                !uiState.isLoading,
            onClick = { onEvent(PasswordResetScreenEvent.SubmitClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))
    }
}
