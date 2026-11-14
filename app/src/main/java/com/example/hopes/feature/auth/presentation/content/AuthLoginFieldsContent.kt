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
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthFindPasswordLink
import com.example.hopes.feature.auth.presentation.component.AuthKeepLoginCheckbox
import com.example.hopes.feature.auth.presentation.component.AuthLoginFailureMessage
import com.example.hopes.feature.auth.presentation.component.AuthLoginTextField
import com.example.hopes.feature.auth.presentation.component.AuthStatusText
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
    val isFailure = failureMessage != null

    Column(modifier = modifier.fillMaxWidth()) {
        AuthFieldLabel(labelRes = R.string.auth_email)

        Spacer(modifier = Modifier.height(8.dp))

        AuthLoginTextField(
            value = emailText,
            onValueChange = onEmailChange,
            placeholderRes = R.string.auth_email,
            isError = isFailure,
            isEnabled = isEnabled,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(8.dp))

        AuthLoginTextField(
            value = passwordText,
            onValueChange = onPasswordChange,
            placeholderRes = R.string.password,
            isError = isFailure,
            isEnabled = isEnabled,
            isPassword = true,
            imeAction = ImeAction.Done,
            onImeAction = onPasswordImeDone,
        )

        if (failureMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))

            AuthLoginFailureMessage(message = failureMessage)
        } else if (statusMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))

            AuthStatusText(
                message = statusMessage.ifBlank { stringResource(R.string.signup_success_message) },
                isError = false,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthKeepLoginCheckbox(
                isChecked = isKeepLoginChecked,
                onCheckedChange = onKeepLoginChange,
            )

            AuthFindPasswordLink(onClick = onFindPasswordClick)
        }
    }
}
