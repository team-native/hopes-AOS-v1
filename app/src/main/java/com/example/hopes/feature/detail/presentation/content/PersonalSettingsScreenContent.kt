package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.detail.presentation.PersonalSettingsScreenEvent
import com.example.hopes.feature.detail.presentation.PersonalSettingsUiState
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.feature.detail.presentation.component.FigmaPersonalSettingsFormCard
import com.example.hopes.navigation.HopesDestination

/** 피그마 13 개인 설정의 시스템 프롬프트 입력 영역을 구성한다. */
@Composable
fun PersonalSettingsScreenContent(
    uiState: PersonalSettingsUiState,
    onEvent: (PersonalSettingsScreenEvent) -> Unit,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        Column {
            FigmaDetailBackHeader(
                title = stringResource(R.string.personal_settings),
                subtitle = stringResource(R.string.personal_settings_subtitle),
                onBackClick = onBackClick,
                backOffsetX = 24,
            )

            Spacer(modifier = Modifier.height(18.dp))

            FigmaPersonalSettingsFormCard(
                personalPrompt = uiState.personalPrompt,
                isPromptLoading = uiState.isPromptLoading,
                isPromptLoadFailed = uiState.isPromptLoadFailed,
                isPromptSaving = uiState.isPromptSaving,
                isPromptSaved = uiState.isPromptSaved,
                isPromptSaveFailed = uiState.isPromptSaveFailed,
                onPersonalPromptChange = {
                    onEvent(PersonalSettingsScreenEvent.PersonalPromptChanged(it))
                },
                onSaveClick = {
                    if (uiState.isPromptLoadFailed) {
                        onEvent(PersonalSettingsScreenEvent.PersonalPromptRetryClicked)
                    } else {
                        onEvent(PersonalSettingsScreenEvent.PersonalPromptSaveClicked)
                    }
                },
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            )
        }
    }
}
