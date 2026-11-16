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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesOfflineBanner
import com.example.hopes.core.designsystem.component.figmaPeekSheetShadow
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthLoginHeading
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.content.AuthLoginFieldsContent
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 시작 화면(01) 위로 올라오는 로그인 바텀시트다(피그마 02).
 *
 * 시작 화면은 그대로 두고 아래에서 시트만 올라온다. 키보드가 올라오거나 로그인 중·실패·오프라인이 되어도
 * 화면 전체가 바뀌지 않고 시트가 같은 자리에서 내용만 바뀐다.
 *
 * - 아래 여백은 키보드 높이를 px로 직접 읽어 그만큼만 띄운다. 키보드가 닫혀 있으면 제스처 바 높이만큼 띄운다.
 * - 시트 바깥이나 입력란이 아닌 곳을 누르면 키보드를 닫는다. 키보드가 닫힌 상태에서 바깥을 누르면 시트를 닫는다.
 *
 * @param onDismiss 시트 바깥을 눌러 닫을 때 부른다(시작 화면으로 돌아간다).
 * @param onSheetHeightChange 시트 높이가 바뀔 때마다 부른다. 시작 화면이 시트에 가려지지 않게 위치를 맞추는 데 쓴다.
 */
@Composable
fun AuthLoginSheet(
    uiState: AuthLoginUiState,
    onEvent: (AuthLoginScreenEvent) -> Unit,
    onDismiss: () -> Unit,
    onSheetHeightChange: (Dp) -> Unit = {},
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

    // 배경이 되는 시작 화면은 AuthScreen이 그대로 그리므로 여기서는 투명하다. 눌림만 받아 키보드·시트를 닫는다.
    Box(modifier = Modifier.fillMaxSize()) {
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
                    // 시트 높이를 알려 시작 화면의 로고·헤드라인이 시트 위 빈 공간에 맞게 올라가도록 한다.
                    .onSizeChanged { size -> onSheetHeightChange(with(density) { size.height.toDp() }) }
                    .fillMaxWidth()
                    .figmaPeekSheetShadow(sheetShape)
                    .background(color = MaterialTheme.colorScheme.surface, shape = sheetShape)
                    // 입력란 밖의 시트 빈 곳을 눌러도 키보드를 닫는다.
                    .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                    .padding(bottom = sheetBottomPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SHEET_HORIZONTAL_PADDING),
        ) {
            // 시트임을 분명히 보여 주는 손잡이다.
            Box(
                modifier =
                    Modifier
                        .padding(top = HANDLE_TOP, bottom = HANDLE_BOTTOM)
                        .align(Alignment.CenterHorizontally)
                        .size(width = HANDLE_WIDTH, height = HANDLE_HEIGHT)
                        .background(LocalHopesExtendedColors.current.authHandle, RoundedCornerShape(HANDLE_HEIGHT)),
            )

            AuthLoginHeading()

            Spacer(modifier = Modifier.height(SHEET_ITEM_GAP))

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

            Spacer(modifier = Modifier.height(SHEET_ITEM_GAP))

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

            Spacer(modifier = Modifier.height(SHEET_ITEM_GAP))

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

// 피그마 02 Sheet: 좌우 패딩 24, 요소 간격 14.
private val SHEET_HORIZONTAL_PADDING = 24.dp
private val SHEET_ITEM_GAP = 14.dp

// 시트 위쪽 손잡이 바 크기와 위아래 간격.
private val HANDLE_WIDTH = 40.dp
private val HANDLE_HEIGHT = 4.dp
private val HANDLE_TOP = 10.dp
private val HANDLE_BOTTOM = 14.dp

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
