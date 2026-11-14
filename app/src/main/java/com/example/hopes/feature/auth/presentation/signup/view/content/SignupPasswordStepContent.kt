package com.example.hopes.feature.auth.presentation.signup.view.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthInlineErrorMessage
import com.example.hopes.feature.auth.presentation.component.AuthTextInputField
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupPasswordRuleList
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupStepTitle
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupScreenEvent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupUiState
import com.teamnative.hopes.R

/** 2단계 비밀번호 설정 화면(03-7 ~ 03-9)이다. 조건 체크리스트와 확인란 일치 여부를 함께 보여준다. */
@Composable
fun SignupPasswordStepContent(
    uiState: SignupUiState,
    onEvent: (SignupScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val ruleErrorMessage =
        if (uiState.isPasswordRuleErrorVisible) {
            stringResource(R.string.signup_password_rule_error)
        } else {
            null
        }
    val mismatchMessage =
        if (uiState.isPasswordMismatchVisible) {
            stringResource(R.string.signup_password_mismatch)
        } else {
            null
        }

    Column(modifier = modifier.fillMaxWidth()) {
        SignupStepTitle(
            text = stringResource(R.string.signup_password_title),
            highlight = stringResource(R.string.signup_password_title_highlight),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthTextInputField(
            value = uiState.password,
            onValueChange = { value -> onEvent(SignupScreenEvent.PasswordChanged(value)) },
            placeholderRes = R.string.password,
            errorMessage = ruleErrorMessage,
            isEnabled = true,
            isPassword = true,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )

        if (ruleErrorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthInlineErrorMessage(message = ruleErrorMessage)
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        SignupPasswordRuleList(
            rules = uiState.passwordRules,
            isErrorVisible = uiState.isPasswordRuleErrorVisible,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        AuthFieldLabel(labelRes = R.string.signup_password_confirm_label)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthTextInputField(
            value = uiState.passwordConfirm,
            onValueChange = { value -> onEvent(SignupScreenEvent.PasswordConfirmChanged(value)) },
            placeholderRes = R.string.signup_password_confirm_placeholder,
            errorMessage = mismatchMessage,
            isEnabled = true,
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = {
                if (uiState.canSubmitPassword) {
                    onEvent(SignupScreenEvent.PrimaryActionClicked)
                }
            },
        )

        if (mismatchMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthInlineErrorMessage(message = mismatchMessage)
        }
    }
}
