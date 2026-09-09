package com.example.hopes.feature.detail.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.detail.presentation.content.PersonalSettingsScreenContent
import com.example.hopes.navigation.HopesDestination

/** 개인 설정 상태와 사용자 의도를 콘텐츠로 전달한다. */
@Composable
fun PersonalSettingsScreen(
    uiState: PersonalSettingsUiState,
    onEvent: (PersonalSettingsScreenEvent) -> Unit,
    onBackClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    PersonalSettingsScreenContent(
        uiState = uiState,
        onEvent = onEvent,
        onBackClick = onBackClick,
        onNavigate = onNavigate,
    )
}
