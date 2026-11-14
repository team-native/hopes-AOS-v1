package com.example.hopes.feature.auth.presentation.signup.view.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesInlineErrorMessage
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthTextInputField
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupCheckMessage
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupCodeInput
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupCodeTimerRow
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupHintText
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupLockedEmailField
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupStepTitle
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SIGNUP_MAX_CODE_ATTEMPT_COUNT
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupCodeError
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupScreenEvent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupUiState
import com.teamnative.hopes.R

/**
 * 1단계 이메일 인증 화면(03 ~ 03-6)이다. 인증번호 발송 전에는 이메일 입력만,
 * 발송 후에는 고정된 이메일 + 6자리 입력 + 타이머를, 인증 후에는 완료 문구를 보여준다.
 */
@Composable
fun SignupEmailStepContent(
    uiState: SignupUiState,
    requestErrorMessage: String?,
    onEvent: (SignupScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val emailErrorMessage =
        if (uiState.isEmailDomainErrorVisible) {
            stringResource(R.string.signup_email_domain_error)
        } else {
            null
        }
    val codeErrorMessage =
        when (val codeError = uiState.codeError) {
            is SignupCodeError.Mismatch -> {
                stringResource(
                    R.string.signup_code_mismatch,
                    codeError.failedAttemptCount,
                    SIGNUP_MAX_CODE_ATTEMPT_COUNT,
                )
            }

            SignupCodeError.AttemptsExceeded -> {
                stringResource(R.string.signup_code_attempts_exceeded)
            }

            SignupCodeError.Expired -> {
                stringResource(R.string.signup_code_expired)
            }

            null -> {
                null
            }
        }

    Column(modifier = modifier.fillMaxWidth()) {
        if (uiState.isCodeSent) {
            SignupStepTitle(
                text = stringResource(R.string.signup_code_title),
                highlight = stringResource(R.string.signup_code_title_highlight),
            )
        } else {
            SignupStepTitle(
                text = stringResource(R.string.signup_email_title),
                highlight = stringResource(R.string.signup_email_title_highlight),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.signup_school_email_label)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        if (uiState.isCodeSent) {
            SignupLockedEmailField(
                email = uiState.email,
                isChangeEnabled = !uiState.isLoading,
                onChangeClick = { onEvent(SignupScreenEvent.ChangeEmailClicked) },
            )
        } else {
            AuthTextInputField(
                value = uiState.email,
                onValueChange = { value -> onEvent(SignupScreenEvent.EmailChanged(value)) },
                placeholderRes = R.string.signup_email_hint,
                errorMessage = emailErrorMessage,
                isEnabled = !uiState.isLoading,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                onImeAction = {
                    if (uiState.canRequestCode) {
                        onEvent(SignupScreenEvent.PrimaryActionClicked)
                    }
                },
            )

            if (emailErrorMessage != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Compact))

                HopesInlineErrorMessage(message = emailErrorMessage)
            }
        }

        if (uiState.isCodeSent) {
            Spacer(modifier = Modifier.height(AppSpacing.Item))

            AuthFieldLabel(labelRes = R.string.signup_code_label)

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            SignupCodeInput(
                code = uiState.code,
                errorMessage =
                    if (uiState.codeError is SignupCodeError.Mismatch) {
                        codeErrorMessage
                    } else {
                        null
                    },
                isEnabled = !uiState.isEmailVerified && !uiState.isCodeLocked && !uiState.isLoading,
                isVerified = uiState.isEmailVerified,
                onCodeChange = { value -> onEvent(SignupScreenEvent.CodeChanged(value)) },
                onDone = {
                    if (uiState.canConfirmCode) {
                        onEvent(SignupScreenEvent.PrimaryActionClicked)
                    }
                },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            if (uiState.isEmailVerified) {
                val verifiedMessage = stringResource(R.string.signup_email_verified)

                SignupCheckMessage(
                    text = verifiedMessage,
                    isChecked = true,
                    color = MaterialTheme.colorScheme.primary,
                    accessibilityText = verifiedMessage,
                    isEmphasized = true,
                )
            } else {
                if (codeErrorMessage != null) {
                    HopesInlineErrorMessage(message = codeErrorMessage)

                    Spacer(modifier = Modifier.height(AppSpacing.Small))
                }

                SignupCodeTimerRow(
                    remainingSeconds = uiState.remainingSeconds,
                    isExpired = uiState.codeError == SignupCodeError.Expired,
                    isResendEnabled = !uiState.isLoading,
                    onResendClick = { onEvent(SignupScreenEvent.ResendCodeClicked) },
                )

                if (codeErrorMessage == null) {
                    Spacer(modifier = Modifier.height(AppSpacing.Small))

                    SignupHintText(text = stringResource(R.string.signup_spam_hint))
                }
            }
        }

        if (requestErrorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesInlineErrorMessage(message = requestErrorMessage)
        }
    }
}
