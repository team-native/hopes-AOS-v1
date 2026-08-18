package com.example.hopes.feature.auth.presentation.passwordreset.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.component.AuthStatusText
import com.example.hopes.feature.auth.presentation.component.AuthStepHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetScreenEvent
import com.example.hopes.feature.auth.presentation.passwordreset.PasswordResetUiState

/** 피그마 13 비밀번호 재설정 화면의 헤더, 이메일 입력, 제출 버튼을 조합한다. */
@Composable
fun PasswordResetScreenContent(
    uiState: PasswordResetUiState,
    onEvent: (PasswordResetScreenEvent) -> Unit,
) {
    // 인증번호 발송에 성공하면 statusMessage가 채워지며, 이후 화면은 인증번호/새 비밀번호 제출 모드로 전환된다.
    val isCodeRequested = uiState.statusMessage != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(horizontal = AppSpacing.ScreenHorizontal),
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

        AuthFieldLabel(labelRes = R.string.verification_code)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.code,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.CodeChanged(value)) },
            labelRes = R.string.verification_code,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.newPassword,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.NewPasswordChanged(value)) },
            labelRes = R.string.password,
            isPassword = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthFieldLabel(labelRes = R.string.password_confirm)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.newPasswordConfirm,
            onValueChange = { value -> onEvent(PasswordResetScreenEvent.NewPasswordConfirmChanged(value)) },
            labelRes = R.string.password_confirm,
            isPassword = true,
            onImeAction = { onEvent(PasswordResetScreenEvent.SubmitClicked) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthPrimaryActionButton(
            text = stringResource(
                if (isCodeRequested) R.string.password_reset_complete else R.string.request_verification_code,
            ),
            isEnabled = if (isCodeRequested) {
                uiState.code.isNotBlank() &&
                    uiState.newPassword.isNotBlank() &&
                    uiState.newPasswordConfirm.isNotBlank() &&
                    !uiState.isLoading
            } else {
                uiState.email.isNotBlank() && !uiState.isLoading
            },
            onClick = {
                onEvent(
                    if (isCodeRequested) {
                        PasswordResetScreenEvent.SubmitClicked
                    } else {
                        PasswordResetScreenEvent.RequestCodeClicked
                    },
                )
            },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))
    }
}
