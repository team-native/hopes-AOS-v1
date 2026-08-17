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
import com.example.hopes.feature.auth.presentation.signupverification.SignUpCodeConfirmationScreenEvent
import com.example.hopes.feature.auth.presentation.signupverification.SignUpCodeConfirmationUiState

/** 회원가입 인증번호 입력 화면의 헤더, 인증번호 입력, 확인 버튼을 조합한다. */
@Composable
fun SignUpCodeConfirmationScreenContent(
    uiState: SignUpCodeConfirmationUiState,
    onEvent: (SignUpCodeConfirmationScreenEvent) -> Unit,
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
            title = stringResource(R.string.signup_code_confirmation_title),
            subtitle = stringResource(R.string.signup_code_confirmation_subtitle),
            onBackClick = { onEvent(SignUpCodeConfirmationScreenEvent.BackClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.verification_code)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        FigmaAuthTextField(
            value = uiState.code,
            onValueChange = { value -> onEvent(SignUpCodeConfirmationScreenEvent.CodeChanged(value)) },
            labelRes = R.string.verification_code,
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
            text = stringResource(R.string.confirm_verification_code),
            isEnabled = uiState.code.isNotBlank() && !uiState.isLoading,
            onClick = { onEvent(SignUpCodeConfirmationScreenEvent.ConfirmClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))
    }
}
