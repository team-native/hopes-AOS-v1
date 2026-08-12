package com.example.hopes.feature.settings.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.navigation.HopesDestination

/** 피그마 11 설정 화면의 설정 행과 로컬 토글 상태를 표시한다. */
@Composable
fun SettingsScreenContent(
    onNavigate: (HopesDestination) -> Unit,
    isDarkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onNavigateToMyPage: () -> Unit,
    onNavigateToPersonalSettings: () -> Unit,
    onNavigateToContact: () -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            modifier = Modifier.offset(x = 24.dp, y = 76.dp),
            style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp),
        )
        Text(
            text = stringResource(R.string.logout_description),
            modifier = Modifier.offset(x = 24.dp, y = 111.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
        )
        FigmaSettingsRow(
            title = stringResource(R.string.personal_settings),
            description = stringResource(R.string.settings_personal_description),
            modifier = Modifier.offset(x = 37.dp, y = 150.dp),
            onClick = onNavigateToPersonalSettings,
        )
        FigmaSettingsRow(
            title = stringResource(R.string.contact),
            description = stringResource(R.string.settings_contact_description),
            modifier = Modifier.offset(x = 37.dp, y = 217.dp),
            onClick = onNavigateToContact,
        )
        FigmaDarkModeRow(
            isEnabled = isDarkModeEnabled,
            modifier = Modifier.offset(x = 39.dp, y = 307.dp),
            onToggle = { onDarkModeChange(!isDarkModeEnabled) },
        )
        Box(
            modifier = Modifier
                .offset(x = 29.dp, y = 397.dp)
                .width(330.dp)
                .height(45.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(14.dp),
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.logout),
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            )
        }
        // 피그마 마이페이지 진입의 상단 액션을 설정 화면에도 로컬 데모로 연결한다.
        Box(
            modifier = Modifier
                .offset(x = 300.dp, y = 76.dp)
                .width(54.dp)
                .height(32.dp)
                .clickable(onClick = onNavigateToMyPage),
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
            modifier = Modifier.offset(x = 20.dp, y = 13.dp),
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = description,
            modifier = Modifier.offset(x = 20.dp, y = 36.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 12.sp),
        )
        Box(
            modifier = Modifier
                .offset(x = 256.dp, y = 16.dp)
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
    Box(
        modifier = modifier
            .width(314.dp)
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle),
    ) {
        Text(
            text = stringResource(R.string.dark_mode),
            modifier = Modifier.offset(x = 20.dp, y = 13.dp),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = stringResource(R.string.settings_description),
            modifier = Modifier.offset(x = 20.dp, y = 38.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 12.sp),
        )
        Box(
            modifier = Modifier
                .offset(x = 257.dp, y = 19.dp)
                .width(44.dp)
                .height(25.dp)
                .background(
                    if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(50.dp),
                ),
        ) {
            Box(
                modifier = Modifier
                    .offset(x = if (isEnabled) 12.dp else 3.dp, y = 3.dp)
                    .width(29.dp)
                    .height(19.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50.dp)),
            )
        }
    }
}
