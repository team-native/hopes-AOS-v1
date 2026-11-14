package com.example.hopes.feature.auth.presentation.signup.view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupStepProgress
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupTopBar
import com.example.hopes.feature.auth.presentation.signup.view.content.SignupEmailStepContent
import com.example.hopes.feature.auth.presentation.signup.view.content.SignupPasswordStepContent
import com.example.hopes.feature.auth.presentation.signup.view.content.SignupProfileStepContent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupCodeError
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupDepartment
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupRequestError
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupScreenEvent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupStep
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupUiState
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 03 ~ 03-10 회원가입 화면이다. 상단 진행 바, 단계별 입력 영역, 하단 버튼으로 구성된다.
 * 하단 버튼은 키보드가 열리면 키보드 바로 위로 올라간다.
 */
@Composable
fun SignupScreen(
    uiState: SignupUiState,
    onEvent: (SignupScreenEvent) -> Unit,
) {
    val requestErrorMessage =
        when (val requestError = uiState.requestError) {
            SignupRequestError.Network -> {
                stringResource(R.string.signup_network_error)
            }

            is SignupRequestError.Server -> {
                requestError.message.ifBlank {
                    stringResource(
                        R.string.generic_error_message,
                    )
                }
            }

            null -> {
                null
            }
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime)),
    ) {
        SignupTopBar(onBackClick = { onEvent(SignupScreenEvent.BackClicked) })

        SignupStepProgress(
            step = uiState.step,
            modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.height(AppSpacing.Section))

            when (uiState.step) {
                SignupStep.Email -> {
                    SignupEmailStepContent(
                        uiState = uiState,
                        requestErrorMessage = requestErrorMessage,
                        onEvent = onEvent,
                    )
                }

                SignupStep.Password -> {
                    SignupPasswordStepContent(
                        uiState = uiState,
                        onEvent = onEvent,
                    )
                }

                SignupStep.Profile -> {
                    SignupProfileStepContent(
                        uiState = uiState,
                        requestErrorMessage = requestErrorMessage,
                        onEvent = onEvent,
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.Section))
        }

        AuthPrimaryActionButton(
            text = stringResource(uiState.primaryActionLabelResId()),
            isEnabled = uiState.isPrimaryActionEnabled(),
            onClick = { onEvent(SignupScreenEvent.PrimaryActionClicked) },
            modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))
    }
}

private fun SignupUiState.primaryActionLabelResId(): Int =
    when (step) {
        SignupStep.Email -> {
            when {
                !isCodeSent -> R.string.signup_request_code
                isEmailVerified -> R.string.signup_next
                isCodeLocked -> R.string.signup_resend_code
                else -> R.string.signup_confirm_code
            }
        }

        SignupStep.Password -> {
            R.string.signup_next
        }

        SignupStep.Profile -> {
            R.string.signup_complete
        }
    }

private fun SignupUiState.isPrimaryActionEnabled(): Boolean =
    when (step) {
        SignupStep.Email -> {
            when {
                !isCodeSent -> canRequestCode
                isEmailVerified -> true
                isCodeLocked -> !isLoading
                else -> canConfirmCode
            }
        }

        SignupStep.Password -> {
            canSubmitPassword
        }

        SignupStep.Profile -> {
            canCompleteSignup
        }
    }

@Preview(showBackground = true, name = "03 이메일 입력")
@Composable
private fun SignupScreenEmailPreview() {
    HopesTheme {
        SignupScreen(
            uiState = SignupUiState(email = "s26055@gsm.hs.kr"),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "03-4 인증번호 틀림")
@Composable
private fun SignupScreenCodeMismatchPreview() {
    HopesTheme {
        SignupScreen(
            uiState =
                SignupUiState(
                    email = "s26055@gsm.hs.kr",
                    isCodeSent = true,
                    code = "482193",
                    codeError = SignupCodeError.Mismatch(failedAttemptCount = 2),
                    failedCodeAttemptCount = 2,
                    remainingSeconds = 221,
                ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "03-8 비밀번호 조건 불일치")
@Composable
private fun SignupScreenPasswordErrorPreview() {
    HopesTheme {
        SignupScreen(
            uiState =
                SignupUiState(
                    step = SignupStep.Password,
                    password = "abc123",
                    passwordConfirm = "abc12",
                ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "03-10 프로필")
@Composable
private fun SignupScreenProfilePreview() {
    HopesTheme {
        SignupScreen(
            uiState =
                SignupUiState(
                    step = SignupStep.Profile,
                    name = "임서하",
                    department = SignupDepartment.ArtificialIntelligence,
                    isGenerationErrorVisible = true,
                    isTermsAgreed = true,
                ),
            onEvent = {},
        )
    }
}
