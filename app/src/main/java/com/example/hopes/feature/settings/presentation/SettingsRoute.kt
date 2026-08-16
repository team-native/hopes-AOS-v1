package com.example.hopes.feature.settings.presentation

import androidx.compose.runtime.Composable
import com.example.hopes.navigation.HopesDestination

/** 설정 화면에 앱 탐색 콜백을 제공한다. */
@Composable
fun SettingsRoute(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
    onLogout: () -> Unit,
) {
    SettingsScreen(
        onNavigate = onNavigate,
        isDarkModeEnabled = isDarkModeEnabled,
        onDarkModeChange = onDarkModeChange,
        onBackClick = onBackClick,
        onNavigateToPersonalSettings = onNavigateToPersonalSettings,
        onNavigateToContact = onNavigateToContact,
        onLogout = onLogout,
    )
}
