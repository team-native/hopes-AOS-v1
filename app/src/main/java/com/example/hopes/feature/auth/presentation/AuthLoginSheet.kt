package com.example.hopes.feature.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesOfflineBanner
import com.example.hopes.core.designsystem.component.figmaSheetShadow
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthLoginHeading
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.content.AuthLoginFieldsContent
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 시작 화면(01) 위로 올라오는 로그인 바텀시트다(피그마 02).
 *
 * - 아래 여백은 키보드 높이를 px로 직접 읽어 그만큼만 띄운다. 키보드가 닫혀 있으면 제스처 바 높이만큼 띄운다.
 * - 시트 바깥이나 입력란이 아닌 곳을 누르면 키보드를 닫는다. 키보드가 닫힌 상태에서 바깥을 누르면 시트를 닫는다.
 *
 * @param onDismiss 시트 바깥을 눌러 닫을 때 부른다(시작 화면으로 돌아간다).
 */
@Composable
fun AuthLoginSheet(
    uiState: AuthLoginUiState,
    onEvent: (AuthLoginScreenEvent) -> Unit,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val navigationBarBottomPx = WindowInsets.navigationBars.getBottom(density)
    val isImeVisible = imeBottomPx > 0
    val sheetBottomPadding = with(density) { maxOf(imeBottomPx, navigationBarBottomPx).toDp() }
    val isLoginEnabled =
        uiState.emailText.isNotBlank() &&
            uiState.passwordText.isNotBlank() &&
            !uiState.isLoading
    val sheetShape = RoundedCornerShape(topStart = AppRadius.Sheet, topEnd = AppRadius.Sheet)

    Box(modifier = Modifier.fillMaxSize()) {
        // 피그마 02처럼 뒤 화면을 어둡게 하지 않는다. 눌림만 받아 키보드·시트를 닫는다.
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .pointerInput(isImeVisible) {
                        detectTapGestures {
                            if (isImeVisible) focusManager.clearFocus() else onDismiss()
                        }
                    },
        )

        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .figmaSheetShadow(sheetShape)
                    .background(color = MaterialTheme.colorScheme.surface, shape = sheetShape)
                    // 입력란 밖의 시트 빈 곳을 눌러도 키보드를 닫는다.
                    .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                    .padding(bottom = sheetBottomPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthLoginHeading()

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthLoginFieldsContent(
                emailText = uiState.emailText,
                passwordText = uiState.passwordText,
                isKeepLoginChecked = uiState.isKeepLoginChecked,
                failureMessage = uiState.failureMessage,
                statusMessage = uiState.statusMessage,
                isEnabled = !uiState.isLoading,
                onEmailChange = { value -> onEvent(AuthLoginScreenEvent.EmailChanged(value)) },
                onPasswordChange = { value -> onEvent(AuthLoginScreenEvent.PasswordChanged(value)) },
                onKeepLoginChange = { isChecked -> onEvent(AuthLoginScreenEvent.KeepLoginChanged(isChecked)) },
                onFindPasswordClick = { onEvent(AuthLoginScreenEvent.FindPasswordClicked) },
                onPasswordImeDone = {
                    if (isLoginEnabled) {
                        onEvent(AuthLoginScreenEvent.LoginClicked)
                    }
                },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthPrimaryActionButton(
                text =
                    if (uiState.isLoading) {
                        stringResource(R.string.auth_login_loading)
                    } else {
                        stringResource(R.string.login)
                    },
                isEnabled = isLoginEnabled,
                onClick = { onEvent(AuthLoginScreenEvent.LoginClicked) },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            AuthLinkPrompt(
                promptText = stringResource(R.string.auth_no_account_prompt),
                linkText = stringResource(R.string.signup),
                onLinkClick = { onEvent(AuthLoginScreenEvent.SignUpClicked) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(AppSpacing.ScreenBottom))
        }

        AnimatedVisibility(
            visible = uiState.isOffline,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = AppSpacing.Item, vertical = AppSpacing.Compact),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            HopesOfflineBanner(message = stringResource(R.string.auth_login_offline_message))
        }
    }
}

@Preview(showBackground = true, name = "02 로그인 시트")
@Composable
private fun AuthLoginSheetPreview() {
    HopesTheme {
        Box {
            AuthStartScreen(onStartClick = {}, onLoginClick = {})
            AuthLoginSheet(uiState = AuthLoginUiState(), onEvent = {}, onDismiss = {})
        }
    }
}

@Preview(showBackground = true, name = "02-6 로그인 시트 오프라인")
@Composable
private fun AuthLoginSheetOfflinePreview() {
    HopesTheme {
        Box {
            AuthStartScreen(onStartClick = {}, onLoginClick = {})
            AuthLoginSheet(
                uiState =
                    AuthLoginUiState(
                        emailText = "s26055@gsm.hs.kr",
                        passwordText = "password",
                        isOffline = true,
                    ),
                onEvent = {},
                onDismiss = {},
            )
        }
    }
}
