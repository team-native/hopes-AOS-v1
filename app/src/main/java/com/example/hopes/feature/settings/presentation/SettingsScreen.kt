package com.example.hopes.feature.settings.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
                icon = Icons.Outlined.DarkMode,
                title = stringResource(R.string.dark_mode),
                description = stringResource(R.string.dark_mode_description),
                isChecked = isDarkModeEnabled,
                onCheckedChange = { onEvent(SettingsScreenEvent.DarkModeChanged(it)) },
            )

            SettingsDivider()

            SettingsToggleRow(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.settings_notification),
                description = stringResource(R.string.settings_notification_description),
                isChecked = isNotificationEnabled,
                onCheckedChange = { onEvent(SettingsScreenEvent.NotificationChanged(it)) },
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        SettingsCard {
            SettingsNavigationRow(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.app_info),
                trailingText = stringResource(R.string.settings_app_version, appVersion),
                onClick = { onEvent(SettingsScreenEvent.AppInfoClicked) },
            )

            SettingsDivider()

            SettingsNavigationRow(
                icon = Icons.Outlined.Shield,
                title = stringResource(R.string.privacy_policy),
                onClick = { onEvent(SettingsScreenEvent.PrivacyPolicyClicked) },
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        SettingsCard {
            SettingsNavigationRow(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = stringResource(R.string.logout),
                contentColor = MaterialTheme.colorScheme.error,
                onClick = { onEvent(SettingsScreenEvent.LogoutClicked) },
            )

            SettingsDivider()

            SettingsNavigationRow(
                icon = Icons.Outlined.PersonRemove,
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
