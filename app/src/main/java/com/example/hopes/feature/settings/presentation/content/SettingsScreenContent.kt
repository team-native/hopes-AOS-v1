package com.example.hopes.feature.settings.presentation.content

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
import com.example.hopes.feature.settings.presentation.component.FigmaDarkModeRow
import com.example.hopes.feature.settings.presentation.component.FigmaSettingsRow
import com.example.hopes.feature.settings.presentation.component.SettingsHeader
import com.example.hopes.feature.settings.presentation.component.SettingsLogoutButton
import com.example.hopes.navigation.HopesDestination

/** 피그마 11 설정 화면의 설정 행과 로컬 토글 상태를 표시한다. */
@Composable
fun SettingsScreenContent(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
    onLogout: () -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        Column {
            SettingsHeader(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(68.dp))

            FigmaSettingsRow(
                title = stringResource(R.string.personal_settings),
                description = stringResource(R.string.settings_personal_description),
                onClick = onNavigateToPersonalSettings,
                modifier = Modifier.padding(start = 37.dp, end = 37.dp),
            )

            Spacer(modifier = Modifier.height(3.dp))

            FigmaSettingsRow(
                title = stringResource(R.string.contact),
                description = stringResource(R.string.settings_contact_description),
                onClick = onNavigateToContact,
                modifier = Modifier.padding(start = 37.dp, end = 37.dp),
            )

            Spacer(modifier = Modifier.height(26.dp))

            FigmaDarkModeRow(
                isEnabled = isDarkModeEnabled,
                onToggle = { onDarkModeChange(!isDarkModeEnabled) },
                modifier = Modifier.padding(start = 39.dp, end = 39.dp),
            )

            Spacer(modifier = Modifier.height(26.dp))

            SettingsLogoutButton(
                onClick = onLogout,
                modifier = Modifier.padding(start = 29.dp, end = 29.dp),
            )
        }
    }
}
