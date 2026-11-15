package com.example.hopes.feature.settings.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.feature.settings.presentation.component.SettingsCard
import com.example.hopes.feature.settings.presentation.component.SettingsDivider
import com.example.hopes.feature.settings.presentation.component.SettingsNavigationRow
import com.example.hopes.feature.settings.presentation.component.SettingsToggleRow
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 32 설정 화면이다. 화면 토글(다크 모드·알림), 앱 정보·처리방침, 계정(로그아웃·회원탈퇴) 카드 세 개로 나눈다.
 *
 * @param appVersion "1.5"처럼 앱 정보 행 오른쪽에 붙일 버전 이름이다.
 */
@Composable
fun SettingsScreen(
    isDarkModeEnabled: Boolean,
    isNotificationEnabled: Boolean,
    isDeleteAccountEnabled: Boolean,
    appVersion: String,
    onEvent: (SettingsScreenEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    HopesSubScreenFrame(
        title = stringResource(R.string.settings_title),
        subtitle = stringResource(R.string.settings_subtitle),
        onBackClick = onBackClick,
    ) {
        SettingsCard {
            SettingsToggleRow(
                icon = painterResource(R.drawable.ic_settings_dark_mode),
                title = stringResource(R.string.dark_mode),
                description = stringResource(R.string.dark_mode_description),
                isChecked = isDarkModeEnabled,
                onCheckedChange = { onEvent(SettingsScreenEvent.DarkModeChanged(it)) },
            )

            SettingsDivider()

            SettingsToggleRow(
                icon = painterResource(R.drawable.ic_menu_notification),
                title = stringResource(R.string.settings_notification),
                description = stringResource(R.string.settings_notification_description),
                isChecked = isNotificationEnabled,
                onCheckedChange = { onEvent(SettingsScreenEvent.NotificationChanged(it)) },
            )
        }

        Spacer(modifier = Modifier.height(CARD_GAP))

        SettingsCard {
            SettingsNavigationRow(
                icon = painterResource(R.drawable.ic_settings_info),
                title = stringResource(R.string.app_info),
                trailingText = stringResource(R.string.settings_app_version, appVersion),
                onClick = { onEvent(SettingsScreenEvent.AppInfoClicked) },
            )

            SettingsDivider()

            SettingsNavigationRow(
                icon = painterResource(R.drawable.ic_settings_info),
                title = stringResource(R.string.privacy_policy),
                onClick = { onEvent(SettingsScreenEvent.PrivacyPolicyClicked) },
            )
        }

        Spacer(modifier = Modifier.height(CARD_GAP))

        // 피그마 Logout Card: 좌우 16(+테두리 1), 위아래 여백 없이 행 위아래 14.
        SettingsCard(
            horizontalPadding = LOGOUT_CARD_PADDING_HORIZONTAL,
            verticalPadding = LOGOUT_CARD_PADDING_VERTICAL,
        ) {
            SettingsNavigationRow(
                icon = painterResource(R.drawable.ic_logout),
                title = stringResource(R.string.logout),
                contentColor = MaterialTheme.colorScheme.error,
                onClick = { onEvent(SettingsScreenEvent.LogoutClicked) },
            )

            SettingsDivider()

            SettingsNavigationRow(
                icon = rememberVectorPainter(Icons.Outlined.PersonRemove),
                title = stringResource(R.string.account_deletion_title),
                contentColor = MaterialTheme.colorScheme.error,
                onClick = {
                    if (isDeleteAccountEnabled) {
                        onEvent(SettingsScreenEvent.DeleteAccountClicked)
                    }
                },
            )
        }
    }
}

@Preview(showBackground = true, name = "32 설정")
@Composable
private fun SettingsScreenPreview() {
    HopesTheme {
        SettingsScreen(
            isDarkModeEnabled = false,
            isNotificationEnabled = true,
            isDeleteAccountEnabled = true,
            appVersion = "1.5",
            onEvent = {},
            onBackClick = {},
        )
    }
}

// 피그마 32의 카드 사이 간격이다(마이페이지와 같음).
private val CARD_GAP = 16.dp

private val LOGOUT_CARD_PADDING_HORIZONTAL = 17.dp
private val LOGOUT_CARD_PADDING_VERTICAL = 1.dp
