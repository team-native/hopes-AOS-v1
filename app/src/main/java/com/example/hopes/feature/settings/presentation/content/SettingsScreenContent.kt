package com.example.hopes.feature.settings.presentation.content

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

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
    val extendedColors = LocalHopesExtendedColors.current

    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        FigmaSettingsBackButton(onClick = onBackClick)
        Text(
            text = stringResource(R.string.settings_title),
            modifier = Modifier.padding(start = 68.dp, top = 25.dp),
            style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp),
        )
        Text(
            text = stringResource(R.string.logout_description),
            modifier = Modifier.padding(start = 74.dp, top = 66.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
        )
        FigmaSettingsRow(
            title = stringResource(R.string.personal_settings),
            description = stringResource(R.string.settings_personal_description),
            modifier = Modifier.padding(start = 37.dp, top = 150.dp),
            onClick = onNavigateToPersonalSettings,
        )
        FigmaSettingsRow(
            title = stringResource(R.string.contact),
            description = stringResource(R.string.settings_contact_description),
            modifier = Modifier.padding(start = 37.dp, top = 217.dp),
            onClick = onNavigateToContact,
        )
        FigmaDarkModeRow(
            isEnabled = isDarkModeEnabled,
            modifier = Modifier.padding(start = 39.dp, top = 307.dp),
            onToggle = { onDarkModeChange(!isDarkModeEnabled) },
        )
        Box(
            modifier = Modifier
                .padding(start = 29.dp, top = 397.dp)
                .width(330.dp)
                .height(45.dp)
                .background(
                    color = extendedColors.logoutContainer,
                    shape = RoundedCornerShape(14.dp),
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                .clickable(onClick = onLogout),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.logout),
                color = extendedColors.logoutText,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

/** 피그마 11의 마이페이지 복귀용 상단 뒤로가기 버튼이다. */
@Composable
private fun FigmaSettingsBackButton(onClick: () -> Unit) {
    val backDescription = stringResource(R.string.back)

    Box(
        modifier = Modifier
            .padding(start = 10.dp, top = 27.dp)
            .width(38.dp)
            .height(38.dp)
            .figmaSubtleShadow(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(13.dp))
            .semantics {
                role = Role.Button
                contentDescription = backDescription
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.width(24.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun FigmaSettingsRow(
    title: String,
    description: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .width(314.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 20.dp, top = 13.dp),
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = description,
            modifier = Modifier.padding(start = 20.dp, top = 36.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 12.sp),
        )
        Box(
            modifier = Modifier
                .padding(start = 256.dp, top = 16.dp)
                .width(48.dp)
                .height(32.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.open),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Composable
private fun FigmaDarkModeRow(
    isEnabled: Boolean,
    modifier: Modifier,
    onToggle: () -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = modifier
            .width(314.dp)
            .height(64.dp)
            .figmaSubtleShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle),
    ) {
        Text(
            text = stringResource(R.string.dark_mode),
            modifier = Modifier.padding(start = 20.dp, top = 13.dp),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = stringResource(R.string.dark_mode_description),
            modifier = Modifier.padding(start = 20.dp, top = 38.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 12.sp),
        )
        Box(
            modifier = Modifier
                .padding(start = 257.dp, top = 19.dp)
                .width(44.dp)
                .height(25.dp)
                .background(
                    if (isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        extendedColors.toggleTrackOff
                    },
                    RoundedCornerShape(50.dp),
                ),
        ) {
            Box(
                modifier = Modifier
                    .padding(start = if (isEnabled) 12.dp else 3.dp, top = 3.dp)
                    .width(29.dp)
                    .height(19.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50.dp)),
            )
        }
    }
}
