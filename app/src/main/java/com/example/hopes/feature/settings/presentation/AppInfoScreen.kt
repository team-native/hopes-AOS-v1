package com.example.hopes.feature.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.feature.settings.presentation.component.SettingsCard
import com.example.hopes.feature.settings.presentation.component.SettingsDivider
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 32-2 앱 정보 화면이다. 로고·버전 아래에 최신 버전·문의 정보를 보여준다.
 * 오픈소스 라이선스·이용약관 행은 연결할 화면이 생기면 추가한다.
 */
@Composable
fun AppInfoScreen(
    appVersion: String,
    onBackClick: () -> Unit,
) {
    HopesSubScreenFrame(
        title = stringResource(R.string.app_info),
        onBackClick = onBackClick,
    ) {
        Spacer(modifier = Modifier.height(AppSpacing.Item))

        AppInfoLogo(modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Text(
            text = stringResource(R.string.app_info_name),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Headline,
        )

        Text(
            text = stringResource(R.string.app_info_version, appVersion),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Subtitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        SettingsCard {
            AppInfoRow(
                label = stringResource(R.string.app_info_latest_version),
                value = stringResource(R.string.app_info_in_use),
            )

            SettingsDivider()

            AppInfoRow(
                label = stringResource(R.string.app_info_contact),
                value = stringResource(R.string.settings_contact_description),
            )
        }

        Spacer(modifier = Modifier.height(FOOTER_TOP_PADDING))

        Text(
            text = stringResource(R.string.app_info_footer),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Caption,
        )
    }
}

@Composable
private fun AppInfoLogo(modifier: Modifier = Modifier) {
    val logoShape = RoundedCornerShape(AppRadius.Card)

    Box(
        modifier =
            modifier
                .size(LOGO_BADGE_SIZE)
                .figmaRaisedShadow(logoShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = logoShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.hopes_logo),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(LOGO_MARK_SIZE),
        )
    }
}

@Composable
private fun AppInfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Body,
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Caption,
        )
    }
}

// 피그마 32-2 로고 배지와 로고, 하단 학교 문구까지의 여백이다.
private val LOGO_BADGE_SIZE = 72.dp
private val LOGO_MARK_SIZE = DpSize(width = 28.dp, height = 40.dp)
private val FOOTER_TOP_PADDING = 160.dp

@Preview(showBackground = true, name = "32-2 앱 정보")
@Composable
private fun AppInfoScreenPreview() {
    HopesTheme {
        AppInfoScreen(
            appVersion = "1.5",
            onBackClick = {},
        )
    }
}
