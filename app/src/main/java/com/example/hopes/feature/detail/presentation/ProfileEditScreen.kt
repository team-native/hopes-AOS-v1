package com.example.hopes.feature.detail.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesCardHeading
import com.example.hopes.core.designsystem.component.HopesErrorBanner
import com.example.hopes.core.designsystem.component.HopesFieldLabel
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.core.designsystem.component.HopesTextArea
import com.example.hopes.core.designsystem.component.HopesTextField
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 26 프로필 수정 화면이다. 수정할 수 없는 계정 정보 카드와 이름·자기소개 입력 카드를 둔다. */
@Composable
fun ProfileEditScreen(
    uiState: ProfileEditUiState,
    onEvent: (ProfileEditScreenEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    HopesSubScreenFrame(
        title = stringResource(R.string.my_page_profile_edit),
        subtitle = stringResource(R.string.profile_edit_subtitle),
        onBackClick = onBackClick,
        bottomButton = {
            if (uiState.isLoadFailed) {
                HopesPrimaryButton(
                    text = stringResource(R.string.profile_edit_retry),
                    onClick = { onEvent(ProfileEditScreenEvent.RetryClicked) },
                )
            } else {
                HopesPrimaryButton(
                    text = stringResource(R.string.save),
                    onClick = { onEvent(ProfileEditScreenEvent.SaveClicked) },
                    isEnabled = uiState.isSaveEnabled,
                )
            }
        },
    ) {
        val bannerMessage =
            when {
                uiState.isLoadFailed -> stringResource(R.string.profile_edit_load_error)
                uiState.isSaveFailed -> stringResource(R.string.profile_edit_save_error)
                else -> null
            }

        if (bannerMessage != null) {
            HopesErrorBanner(message = bannerMessage)

            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }

        HopesSurfaceCard {
            HopesCardHeading(
                title = stringResource(R.string.profile_edit_account_title),
                description = stringResource(R.string.profile_edit_account_description),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            ProfileAccountRow(
                label = stringResource(R.string.profile_edit_email),
                value = uiState.email,
            )

            ProfileAccountRow(
                label = stringResource(R.string.profile_edit_major),
                value = uiState.major.orEmpty(),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        HopesSurfaceCard {
            HopesCardHeading(title = stringResource(R.string.profile))

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            HopesFieldLabel(text = stringResource(R.string.profile_edit_name))

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesTextField(
                value = uiState.name,
                onValueChange = { onEvent(ProfileEditScreenEvent.NameChanged(it)) },
                placeholder = stringResource(R.string.profile_edit_name_placeholder),
                accessibilityLabel = stringResource(R.string.profile_edit_name),
                isEnabled = !uiState.isLoading && !uiState.isSaving,
                imeAction = ImeAction.Next,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            HopesFieldLabel(text = stringResource(R.string.profile_personalization_label))

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesTextArea(
                value = uiState.introduction,
                onValueChange = { onEvent(ProfileEditScreenEvent.IntroductionChanged(it)) },
                placeholder = stringResource(R.string.profile_placeholder),
                accessibilityLabel = stringResource(R.string.profile_personalization_label),
                isEnabled = !uiState.isLoading && !uiState.isSaving,
                minHeight = INTRODUCTION_MIN_HEIGHT,
            )
        }
    }
}

@Composable
private fun ProfileAccountRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = AppSpacing.Small + AppSpacing.XSmall),
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

private val INTRODUCTION_MIN_HEIGHT = AppSpacing.Large * 3

@Preview(showBackground = true, name = "26 프로필 수정")
@Composable
private fun ProfileEditScreenPreview() {
    HopesTheme {
        ProfileEditScreen(
            uiState =
                ProfileEditUiState(
                    name = "임서하",
                    email = "s26055@gsm.hs.kr",
                    major = "인공지능소프트웨어과",
                    isLoading = false,
                ),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "26-3 프로필 저장 실패")
@Composable
private fun ProfileEditScreenSaveFailedPreview() {
    HopesTheme {
        ProfileEditScreen(
            uiState =
                ProfileEditUiState(
                    name = "임서하",
                    introduction = "프론트엔드에 관심 많은 10기 학생이에요.",
                    email = "s26055@gsm.hs.kr",
                    major = "인공지능소프트웨어과",
                    isLoading = false,
                    isSaveFailed = true,
                ),
            onEvent = {},
            onBackClick = {},
        )
    }
}
