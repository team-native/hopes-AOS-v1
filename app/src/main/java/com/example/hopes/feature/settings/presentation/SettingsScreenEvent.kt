package com.example.hopes.feature.settings.presentation

/** 설정 화면에서 발생하는 사용자 의도를 정의한다. */
sealed interface SettingsScreenEvent {
    data class DarkModeChanged(
        val isEnabled: Boolean,
    ) : SettingsScreenEvent

    data class NotificationChanged(
        val isEnabled: Boolean,
    ) : SettingsScreenEvent

    data object AppInfoClicked : SettingsScreenEvent

    data object PrivacyPolicyClicked : SettingsScreenEvent

    data object LogoutClicked : SettingsScreenEvent

    data object DeleteAccountClicked : SettingsScreenEvent
}
