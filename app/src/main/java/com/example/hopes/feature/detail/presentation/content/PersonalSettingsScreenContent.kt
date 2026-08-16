package com.example.hopes.feature.detail.presentation.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.feature.detail.presentation.component.FigmaPersonalSettingsFormCard
import com.example.hopes.navigation.HopesDestination

/** 피그마 13 개인 설정의 시스템 프롬프트 입력 영역을 구성한다. */
@Composable
fun PersonalSettingsScreenContent(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        FigmaDetailBackHeader(
            title = stringResource(R.string.personal_settings),
            subtitle = stringResource(R.string.personal_settings_subtitle),
            onBackClick = { onEvent(DetailScreenEvent.BackClicked) },
        )
        FigmaPersonalSettingsFormCard(
            personalPrompt = uiState.personalPrompt,
            isPromptLoading = uiState.isPromptLoading,
            isPromptLoadError = uiState.isPromptLoadError,
            isPromptSaving = uiState.isPromptSaving,
            isPromptSaved = uiState.isPromptSaved,
            isPromptSaveError = uiState.isPromptSaveError,
            onPersonalPromptChange = {
                onEvent(DetailScreenEvent.PersonalPromptChanged(it))
            },
            onSaveClick = { onEvent(DetailScreenEvent.PersonalPromptSaveClicked) },
        )
    }
}
