package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthCheckboxRow
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthFindPasswordLink
import com.example.hopes.feature.auth.presentation.component.AuthInlineErrorMessage
import com.example.hopes.feature.auth.presentation.component.AuthStatusText
import com.example.hopes.feature.auth.presentation.component.AuthTextInputField
import com.teamnative.hopes.R

/** 로그인 화면의 이메일·비밀번호 입력, 로그인 유지/비밀번호 찾기 행, 실패·안내 문구를 묶는다. */
@Composable
fun AuthLoginFieldsContent(
    emailText: String,
    passwordText: String,
    isKeepLoginChecked: Boolean,
    failureMessage: String?,
    statusMessage: String?,
    isEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onKeepLoginChange: (Boolean) -> Unit,
    onFindPasswordClick: () -> Unit,
    onPasswordImeDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    // 서버가 실패 사유를 보내지 않으면(빈 문자열) 기본 실패 문구로 대신한다.
    val displayedFailureMessage = failureMessage?.ifBlank { stringResource(R.string.auth_login_failure_message) }

    Column(modifier = modifier.fillMaxWidth()) {
        AuthFieldLabel(labelRes = R.string.auth_email)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthTextInputField(
            value = emailText,
            onValueChange = onEmailChange,
            placeholderRes = R.string.auth_email,
            keyboardType = KeyboardType.Email,
            errorMessage = displayedFailureMessage,
            isEnabled = isEnabled,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthTextInputField(
            value = passwordText,
            onValueChange = onPasswordChange,
            placeholderRes = R.string.password,
            errorMessage = displayedFailureMessage,
            isEnabled = isEnabled,
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = onPasswordImeDone,
        )

        if (displayedFailureMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthInlineErrorMessage(message = displayedFailureMessage)
        } else if (statusMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            AuthStatusText(
                message = statusMessage.ifBlank { stringResource(R.string.signup_success_message) },
                isError = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthCheckboxRow(
                text = stringResource(R.string.auth_keep_login),
                isChecked = isKeepLoginChecked,
                onCheckedChange = onKeepLoginChange,
            )

            AuthFindPasswordLink(onClick = onFindPasswordClick)
        }
    }
}
