package com.example.hopes.feature.settings.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.feature.settings.presentation.content.SettingsScreenContent
import com.example.hopes.navigation.HopesDestination

/** 설정 목록 콘텐츠를 화면 단위로 제공한다. */
@Composable
fun SettingsScreen(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToMyPage: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
) {
    SettingsScreenContent(
        onNavigate = onNavigate,
        isDarkModeEnabled = isDarkModeEnabled,
        onDarkModeChange = onDarkModeChange,
        onNavigateToMyPage = onNavigateToMyPage,
        onNavigateToPersonalSettings = onNavigateToPersonalSettings,
        onNavigateToContact = onNavigateToContact,
    )
}
