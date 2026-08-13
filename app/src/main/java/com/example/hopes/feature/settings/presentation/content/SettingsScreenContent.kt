package com.example.hopes.feature.settings.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

@Composable
fun SettingsScreenContent(onNavigate: (HopesDestination) -> Unit, isDarkModeEnabled: Boolean, onDarkModeChange: (Boolean) -> Unit, onBackClick: () -> Unit, onNavigateToPersonalSettings: () -> Unit, onNavigateToContact: () -> Unit, onLogout: () -> Unit) {
    val colors = LocalHopesExtendedColors.current
    HopesScaffold(HopesDestination.Settings, onNavigate) { inner -> Column(Modifier.fillMaxSize().padding(inner).padding(horizontal = AppSpacing.ScreenHorizontal, vertical = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.settings_title), style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold)); Text(stringResource(R.string.logout_description), color = MaterialTheme.colorScheme.onSurfaceVariant)
        SettingsRow(stringResource(R.string.personal_settings), stringResource(R.string.settings_personal_description), onNavigateToPersonalSettings)
        SettingsRow(stringResource(R.string.contact), stringResource(R.string.settings_contact_description), onNavigateToContact)
        Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)).clickable { onDarkModeChange(!isDarkModeEnabled) }.padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column { Text(stringResource(R.string.dark_mode), fontWeight = FontWeight.SemiBold); Text(stringResource(R.string.dark_mode_description), color = MaterialTheme.colorScheme.onSurfaceVariant) }; Text(if (isDarkModeEnabled) "ON" else "OFF", color = MaterialTheme.colorScheme.primary) }
        Text(stringResource(R.string.logout), Modifier.fillMaxWidth().background(colors.logoutContainer, RoundedCornerShape(14.dp)).clickable(onClick = onLogout).padding(14.dp), color = colors.logoutText, style = TextStyle(fontWeight = FontWeight.SemiBold))
    } }
}

@Composable private fun SettingsRow(title: String, description: String, onClick: () -> Unit) { Row(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) { Column { Text(title, fontWeight = FontWeight.SemiBold); Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = TextStyle(fontSize = 12.sp)) }; Text(stringResource(R.string.open), color = MaterialTheme.colorScheme.primary) } }
