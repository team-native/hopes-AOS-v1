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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesOfflineBanner
import com.example.hopes.core.designsystem.component.figmaPeekSheetShadow
import com.example.hopes.core.designsystem.component.figmaStatusBarsPadding
import com.example.hopes.feature.auth.presentation.component.AuthHeroHeadline
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthLoginHeading
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.content.AuthLoginFieldsContent
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 피그마 02 로그인 바텀시트다. 시트 위 빈 공간에는 28sp 헤드라인만 보이고 시작 화면의 나머지는 가려진다.
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

    // 피그마 02-2·02-3·02-4·02-5·02-6: 입력 중(키보드)·로그인 중·실패·오프라인이면 시트가 화면 전체로
    // 펼쳐지고 헤드라인은 사라진다. 입력란 포커스가 유지되도록 같은 트리에서 모양만 바꾼다.
    val isExpanded =
        isImeVisible ||
            uiState.isLoading ||
            uiState.failureMessage != null ||
            uiState.isOffline

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isExpanded) {
                // 피그마 02: 시트 위 빈 공간 가운데에 28sp 헤드라인만 보인다(로고·설명·버튼·구름은 없다).
                // 눌림만 받아 시트를 닫는다.
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .figmaStatusBarsPadding()
                            .pointerInput(Unit) { detectTapGestures { onDismiss() } }
                            .padding(horizontal = AppSpacing.ScreenHorizontal),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AuthHeroHeadline(style = HopesTextStyles.Display)
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .then(if (isExpanded) Modifier.weight(1f).figmaStatusBarsPadding() else Modifier)
                        .then(if (isExpanded) Modifier else Modifier.figmaPeekSheetShadow(sheetShape))
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = if (isExpanded) RectangleShape else sheetShape,
                        )
                        // 입력란 밖의 시트 빈 곳을 눌러도 키보드를 닫는다.
                        .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                        .padding(bottom = sheetBottomPadding)
                        .then(if (isExpanded) Modifier else Modifier.verticalScroll(rememberScrollState()))
                        .padding(
                            horizontal = if (isExpanded) EXPANDED_HORIZONTAL_PADDING else SHEET_HORIZONTAL_PADDING,
                        ),
            ) {
                if (!isExpanded) {
                    // 시트임을 분명히 보여 주는 손잡이다(피그마에는 없는 요소).
                    Box(
                        modifier =
                            Modifier
                                .padding(top = HANDLE_TOP, bottom = HANDLE_BOTTOM)
                                .align(Alignment.CenterHorizontally)
                                .size(width = HANDLE_WIDTH, height = HANDLE_HEIGHT)
                                .background(
                                    LocalHopesExtendedColors.current.authHandle,
                                    RoundedCornerShape(HANDLE_HEIGHT),
                                ),
                    )
                } else {
                    Spacer(
                        modifier =
                            Modifier.height(
                                if (uiState.isOffline) EXPANDED_TOP_GAP + OFFLINE_TITLE_OFFSET else EXPANDED_TOP_GAP,
                            ),
                    )
                }

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
                    showKeepLoginRow = !isExpanded,
                    onPasswordImeDone = {
                        if (isLoginEnabled) {
                            onEvent(AuthLoginScreenEvent.LoginClicked)
                        }
                    },
                )

                // 펼친 상태에서 키보드가 없으면 버튼과 링크를 맨 아래로 붙이고(02-4·02-5), 그 밖에는 폼 바로 아래에 둔다.
                if (isExpanded && !isImeVisible) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.height(SHEET_ITEM_GAP))
                }

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

                // 피그마 02-2·02-3(키보드)에는 회원가입 링크가 없다.
                if (!isImeVisible) {
                    Spacer(modifier = Modifier.height(SHEET_ITEM_GAP))

                    AuthLinkPrompt(
                        promptText = stringResource(R.string.auth_no_account_prompt),
                        linkText = stringResource(R.string.signup),
                        onLinkClick = { onEvent(AuthLoginScreenEvent.SignUpClicked) },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            if (isExpanded) SHEET_ITEM_GAP + AppSpacing.XSmall else AppSpacing.ScreenBottom,
                        ),
                )
            }
        }

        AnimatedVisibility(
            visible = uiState.isOffline,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .figmaStatusBarsPadding()
                    .padding(horizontal = EXPANDED_HORIZONTAL_PADDING, vertical = OFFLINE_BANNER_TOP),
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

// 피그마 02-2~02-6(펼친 상태): 좌우 20, 제목 위 16, 오프라인 배너가 있으면 제목이 57 내려간다.
private val EXPANDED_HORIZONTAL_PADDING = 20.dp
private val EXPANDED_TOP_GAP = 16.dp
private val OFFLINE_TITLE_OFFSET = 57.dp
private val OFFLINE_BANNER_TOP = 8.dp

// 기본 시트 위쪽 손잡이 바 크기와 위아래 간격.
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
