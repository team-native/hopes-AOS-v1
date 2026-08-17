package com.example.hopes.feature.auth.presentation.signupverification.content

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
import com.example.hopes.feature.auth.presentation.signupverification.SignUpEmailVerificationScreenEvent
import com.example.hopes.feature.auth.presentation.signupverification.SignUpEmailVerificationUiState

/** 회원가입 이메일 인증번호 발송 화면의 헤더, 이메일 입력, 제출 버튼을 조합한다. */
@Composable
fun SignUpEmailVerificationScreenContent(
    uiState: SignUpEmailVerificationUiState,
    onEvent: (SignUpEmailVerificationScreenEvent) -> Unit,
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
            title = stringResource(R.string.signup_email_verification_title),
            subtitle = stringResource(R.string.signup_email_verification_subtitle),
            onBackClick = { onEvent(SignUpEmailVerificationScreenEvent.BackClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.auth_email)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.email,
            onValueChange = { value -> onEvent(SignUpEmailVerificationScreenEvent.EmailChanged(value)) },
            labelRes = R.string.auth_email,
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthStatusText(
                message = stringResource(R.string.generic_error_message),
                isError = true,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AuthPrimaryActionButton(
            text = stringResource(R.string.request_verification_code),
            isEnabled = uiState.email.isNotBlank() && !uiState.isLoading,
            onClick = { onEvent(SignUpEmailVerificationScreenEvent.RequestCodeClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))
    }
}
