package com.example.hopes.feature.detail.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesCardHeading
import com.example.hopes.core.designsystem.component.HopesErrorBanner
import com.example.hopes.core.designsystem.component.HopesHeaderAction
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.core.designsystem.component.HopesTextArea
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 28 개인 설정 화면이다. 시스템 프롬프트 입력 카드 하나와 저장 버튼(헤더·하단)을 둔다. */
@Composable
fun PersonalSettingsScreen(
    uiState: PersonalSettingsUiState,
    onEvent: (PersonalSettingsScreenEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val onSaveClick = { onEvent(PersonalSettingsScreenEvent.PersonalPromptSaveClicked) }

    HopesSubScreenFrame(
        title = stringResource(R.string.personal_settings),
        subtitle = stringResource(R.string.personal_settings_subtitle),
        onBackClick = onBackClick,
        headerAction =
            HopesHeaderAction(
                text = stringResource(R.string.save),
                onClick = onSaveClick,
                isEnabled = uiState.isSaveEnabled,
            ),
        bottomButton = {
            if (uiState.isPromptLoadFailed) {
                HopesPrimaryButton(
                    text = stringResource(R.string.personal_settings_retry),
                    onClick = { onEvent(PersonalSettingsScreenEvent.PersonalPromptRetryClicked) },
                )
            } else {
                HopesPrimaryButton(
                    text = stringResource(R.string.prompt_save),
                    onClick = onSaveClick,
                    isEnabled = uiState.isSaveEnabled,
                )
            }
        },
        toast =
            if (uiState.isPromptSaved) {
                {
                    HopesToast(
                        message = stringResource(R.string.personal_settings_saved_toast),
                        onDismiss = { onEvent(PersonalSettingsScreenEvent.SavedToastDismissed) },
                    )
                }
            } else {
                null
            },
    ) {
        val bannerMessage =
            when {
                uiState.isPromptLoadFailed -> stringResource(R.string.personal_settings_load_error)
                uiState.isPromptSaveFailed -> stringResource(R.string.personal_settings_save_error)
                else -> null
            }

        if (bannerMessage != null) {
            HopesErrorBanner(message = bannerMessage)

            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }

        HopesSurfaceCard {
            HopesCardHeading(
                title = stringResource(R.string.personal_settings_prompt_title),
                description = stringResource(R.string.personal_settings_prompt_description),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            HopesTextArea(
                value = uiState.personalPrompt,
                onValueChange = { onEvent(PersonalSettingsScreenEvent.PersonalPromptChanged(it)) },
                placeholder = stringResource(R.string.prompt_placeholder),
                accessibilityLabel = stringResource(R.string.personal_settings_prompt_title),
                isEnabled = !uiState.isPromptLoading && !uiState.isPromptSaving,
                minHeight = PROMPT_MIN_HEIGHT,
            )
        }
    }
}

// 피그마 28 시스템 프롬프트 입력란의 최소 높이다.
private val PROMPT_MIN_HEIGHT = 200.dp

@Preview(showBackground = true, name = "28 개인 설정")
@Composable
private fun PersonalSettingsScreenPreview() {
    HopesTheme {
        PersonalSettingsScreen(
            uiState = PersonalSettingsUiState(isPromptLoading = false),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "28-3 개인 설정 저장 완료")
@Composable
private fun PersonalSettingsScreenSavedPreview() {
    HopesTheme {
        PersonalSettingsScreen(
            uiState =
                PersonalSettingsUiState(
                    personalPrompt = "답변은 3문장 이내로 짧게, 예시를 하나씩 들어 줘.",
                    isPromptLoading = false,
                    isPromptSaved = true,
                ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
