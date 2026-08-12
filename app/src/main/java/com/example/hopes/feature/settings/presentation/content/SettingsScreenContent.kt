package com.example.hopes.feature.settings.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.settings.presentation.component.SettingsItem
import com.example.hopes.navigation.HopesDestination

/** 설정 안내 항목을 화면에 조합한다. */
@Composable
fun SettingsScreenContent(
    onNavigate: (HopesDestination) -> Unit,
    onNavigateToMyPage: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
) {
    HopesScaffold(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = AppSpacing.ScreenHorizontal,
                    vertical = AppSpacing.ScreenVertical,
                ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        ) {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.settings_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            SettingsItem(text = stringResource(R.string.my_page), onClick = onNavigateToMyPage)
            SettingsItem(text = stringResource(R.string.personal_settings), onClick = onNavigateToPersonalSettings)
            SettingsItem(text = stringResource(R.string.contact), onClick = onNavigateToContact)
            SettingsItem(text = stringResource(R.string.dark_mode), onClick = {})
        }
    }
}
