package com.example.hopes.feature.settings.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.settings.presentation.component.SettingsItem
import com.example.hopes.navigation.HopesDestination

/** 설정 항목을 화면 크기에 맞춰 세로 목록으로 배치한다. */
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
    HopesScaffold(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            Text(text = stringResource(R.string.settings_title))
            SettingsItem(
                text = stringResource(R.string.personal_settings),
                description = stringResource(R.string.settings_personal_description),
                onClick = onNavigateToPersonalSettings,
            )
            SettingsItem(
                text = stringResource(R.string.contact),
                description = stringResource(R.string.settings_contact_description),
                onClick = onNavigateToContact,
            )
            Switch(
                checked = isDarkModeEnabled,
                onCheckedChange = onDarkModeChange,
            )
            Button(onClick = onLogout) { Text(text = stringResource(R.string.logout)) }
        }
    }
}
