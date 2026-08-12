package com.example.hopes.feature.settings.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.navigation.HopesDestination

/** 설정 화면에 앱 탐색 콜백을 제공한다. */
@Composable
fun SettingsRoute(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToMyPage: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
) {
    SettingsScreen(
        onNavigate = onNavigate,
        isDarkModeEnabled = isDarkModeEnabled,
        onDarkModeChange = onDarkModeChange,
        onNavigateToMyPage = onNavigateToMyPage,
        onNavigateToPersonalSettings = onNavigateToPersonalSettings,
        onNavigateToContact = onNavigateToContact,
    )
}
