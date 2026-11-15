package com.example.hopes.feature.auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesOfflineBanner
import com.example.hopes.core.designsystem.component.figmaSheetShadow
import com.example.hopes.feature.auth.presentation.component.AuthHeroHeadline
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthLoginHeading
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.content.AuthLoginFieldsContent
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 02 로그인 화면이다.
 *
 * - 처음 진입(입력 전·키보드 닫힘): 상단 헤드라인 + 하단 카드 (02)
 * - 입력 중이거나 로딩·실패·오프라인 상태: 헤드라인을 숨기고 폼을 화면 위쪽에 둔다 (02-2 ~ 02-6)
 *
 * 두 상태는 같은 Composable 트리 안에서 헤드라인 표시 여부와 여백만 바뀐다. 레이아웃을 통째로
 * 교체하면 입력 중인 필드의 포커스가 사라져 키보드가 닫히기 때문이다.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthLoginScreen(
    uiState: AuthLoginUiState,
    onEvent: (AuthLoginScreenEvent) -> Unit,
) {
    val isImeVisible = WindowInsets.isImeVisible
    val isIntroLayout =
        !isImeVisible &&
            uiState.emailText.isEmpty() &&
            uiState.passwordText.isEmpty() &&
            uiState.failureMessage == null &&
            !uiState.isOffline
    val isLoginEnabled =
        uiState.emailText.isNotBlank() &&
            uiState.passwordText.isNotBlank() &&
            !uiState.isLoading
    val cardCornerRadius by animateDpAsState(
        targetValue =
            if (isIntroLayout) {
                AppRadius.Sheet
            } else {
                0.dp
            },
        label = "loginCardCornerRadius",
    )
    val cardShape =
        RoundedCornerShape(
            topStart = cardCornerRadius,
            topEnd = cardCornerRadius,
        )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)),
        ) {
            AnimatedVisibility(
                visible = isIntroLayout,
                modifier =
                    if (isIntroLayout) {
                        Modifier.weight(1f)
                    } else {
                        Modifier
                    },
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = AppSpacing.ScreenHorizontal),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    AuthHeroHeadline()
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .then(
                            if (isIntroLayout) {
                                Modifier
                            } else {
                                Modifier.weight(1f)
                            },
                        ).figmaSheetShadow(cardShape)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = cardShape,
                        ).padding(horizontal = AppSpacing.ScreenHorizontal),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .then(
                                if (isIntroLayout) {
                                    Modifier
                                } else {
                                    Modifier.weight(1f, fill = false)
                                },
                            ).verticalScroll(rememberScrollState()),
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
                        onKeepLoginChange = { isChecked ->
                            onEvent(AuthLoginScreenEvent.KeepLoginChanged(isChecked))
                        },
                        onFindPasswordClick = { onEvent(AuthLoginScreenEvent.FindPasswordClicked) },
                        onPasswordImeDone = {
                            if (isLoginEnabled) {
                                onEvent(AuthLoginScreenEvent.LoginClicked)
                            }
                        },
                    )
                }

                if (isIntroLayout || isImeVisible) {
                    Spacer(modifier = Modifier.height(AppSpacing.Section))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
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

                // 키보드가 열려 있으면 피그마(02-2, 02-3)처럼 회원가입 유도 문구를 숨겨 입력 영역을 확보한다.
                if (!isImeVisible) {
                    Spacer(modifier = Modifier.height(AppSpacing.Compact))

                    AuthLinkPrompt(
                        promptText = stringResource(R.string.auth_no_account_prompt),
                        linkText = stringResource(R.string.signup),
                        onLinkClick = { onEvent(AuthLoginScreenEvent.SignUpClicked) },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Compact))
            }
        }

        AnimatedVisibility(
            visible = uiState.isOffline,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(
                        horizontal = AppSpacing.Item,
                        vertical = AppSpacing.Compact,
                    ),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            HopesOfflineBanner(message = stringResource(R.string.auth_login_offline_message))
        }
    }
}

@Preview(showBackground = true, name = "02 로그인")
@Composable
private fun AuthLoginScreenIntroPreview() {
    HopesTheme {
        AuthLoginScreen(
            uiState = AuthLoginUiState(),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "02-5 로그인 실패")
@Composable
private fun AuthLoginScreenFailurePreview() {
    HopesTheme {
        AuthLoginScreen(
            uiState =
                AuthLoginUiState(
                    emailText = "s26055@gsm.hs.kr",
                    passwordText = "password",
                    failureMessage = "",
                ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "02-6 오프라인")
@Composable
private fun AuthLoginScreenOfflinePreview() {
    HopesTheme {
        AuthLoginScreen(
            uiState =
                AuthLoginUiState(
                    emailText = "s26055@gsm.hs.kr",
                    passwordText = "password",
                    isOffline = true,
                ),
            onEvent = {},
        )
    }
}
