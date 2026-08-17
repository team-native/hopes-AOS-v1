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

        if (uiState.statusMessage != null || uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthStatusText(
                message = if (uiState.statusMessage != null) {
                    stringResource(R.string.verification_sent_message)
                } else {
                    stringResource(R.string.generic_error_message)
                },
                isError = uiState.statusMessage == null,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AuthPrimaryActionButton(
            text = stringResource(R.string.request_verification_code),
            isEnabled = uiState.email.isNotBlank() && !uiState.isLoading,
            onClick = { onEvent(PasswordResetScreenEvent.RequestCodeClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))
    }
}
