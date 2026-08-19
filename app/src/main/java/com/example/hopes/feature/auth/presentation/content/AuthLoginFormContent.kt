package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.input.pointer.pointerInput
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
    onHandleDrag: (Float) -> Unit,
    onHandleDragEnd: () -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val isLoginEnabled = emailText.isNotBlank() && passwordText.isNotBlank()

    // imePadding을 verticalScroll보다 바깥쪽에 둬야 키보드 높이만큼 뷰포트 자체가 줄어들어,
    // verticalScroll이 실제로 남은 공간을 정확히 알고 그 안에서 포커스된 필드를 끝까지
    // 끌어올릴 수 있다.
    Column(
        modifier = Modifier
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // 핸들에만 드래그 제스처를 붙여, 아래 필드 목록의 verticalScroll과 제스처가
        // 충돌해 시트를 끌어내릴 수 없게 되는 문제를 막는다.
        Box(
            modifier = Modifier.pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onHandleDrag(dragAmount.y)
                    },
                    onDragEnd = { onHandleDragEnd() },
                )
            },
        ) {
            AuthSheetHandle()
        }

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
