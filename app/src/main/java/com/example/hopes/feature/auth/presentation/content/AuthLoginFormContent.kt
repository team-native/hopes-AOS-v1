package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthForgotPasswordLink
import com.example.hopes.feature.auth.presentation.component.AuthLoginErrorMessage
import com.example.hopes.feature.auth.presentation.component.AuthSheetHandle
import com.example.hopes.feature.auth.presentation.component.AuthSheetHeading
import com.example.hopes.feature.auth.presentation.component.AuthSignupPrompt
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.feature.auth.presentation.component.FigmaLoginButton
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 로그인 시트 안쪽의 입력 폼이다. 핸들부터 회원가입 유도 문구까지 세로로 배치한다. */
@Composable
fun AuthLoginFormContent(
    emailText: String,
    passwordText: String,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val isLoginEnabled = emailText.isNotBlank() && passwordText.isNotBlank()

    // 키보드가 열리면 imePadding이 하단 여백을 확보하고, verticalScroll이 포커스된 필드가
    // 보이는 영역까지 자동으로 스크롤한다. 매직넘버로 시트를 밀어올리던 기존 방식을 대체한다.
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        AuthSheetHandle()

        Spacer(modifier = Modifier.height(43.dp))

        AuthSheetHeading(
            title = stringResource(R.string.login),
            titleColor = MaterialTheme.colorScheme.onSurface,
            titleFontSize = 26.sp,
            subtitle = stringResource(R.string.login_subtitle),
            subtitleColor = extendedColors.authFieldHint,
            subtitleFontSize = 14.sp,
            subtitleLineHeight = 20.sp,
            spacing = 9.dp,
        )

        Spacer(modifier = Modifier.height(34.dp))

        AuthFieldLabel(labelRes = R.string.auth_email)

        Spacer(modifier = Modifier.height(7.dp))

        FigmaAuthTextField(
            value = emailText,
            onValueChange = onEmailChange,
            labelRes = R.string.auth_email,
            modifier = Modifier.width(332.dp),
        )

        Spacer(modifier = Modifier.height(15.dp))

        AuthFieldLabel(labelRes = R.string.password)

        Spacer(modifier = Modifier.height(9.dp))

        FigmaAuthTextField(
            value = passwordText,
            onValueChange = onPasswordChange,
            labelRes = R.string.password,
            isPassword = true,
            onImeAction = if (isLoginEnabled) onLoginClick else null,
            modifier = Modifier.width(332.dp),
        )

        Spacer(modifier = Modifier.height(13.dp))

        AuthForgotPasswordLink(
            onClick = onForgotPasswordClick,
            modifier = Modifier.width(332.dp),
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))

            AuthLoginErrorMessage(modifier = Modifier.width(332.dp))
        }

        Spacer(modifier = Modifier.height(27.dp))

        FigmaLoginButton(
            isEnabled = isLoginEnabled,
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(21.dp))

        AuthSignupPrompt(onClick = onNavigateSignup)
    }
}
