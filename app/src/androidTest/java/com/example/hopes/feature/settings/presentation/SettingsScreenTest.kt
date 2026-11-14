package com.example.hopes.feature.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun settingsScreen_showsAllRowsAndKeepsAccountDeletion() {
        setSettingsContent(onEvent = {})

        composeRule.onNodeWithText("다크 모드").assertIsDisplayed()
        composeRule.onNodeWithText("알림 받기").assertIsDisplayed()
        composeRule.onNodeWithText("앱 정보").assertIsDisplayed()
        composeRule.onNodeWithText("v1.5").assertIsDisplayed()
        composeRule.onNodeWithText("개인정보 처리방침").assertIsDisplayed()
        composeRule.onNodeWithText("로그아웃").assertIsDisplayed()
        composeRule.onNodeWithText("회원탈퇴").assertIsDisplayed()
    }

    @Test
    fun rowClicks_sendMatchingEvents() {
        val events = mutableListOf<SettingsScreenEvent>()
        setSettingsContent(onEvent = { events += it })

        composeRule.onNodeWithText("로그아웃").performClick()
        composeRule.onNodeWithText("앱 정보").performClick()
        composeRule.onNodeWithText("다크 모드").performClick()

        composeRule.runOnIdle {
            assertEquals(
                listOf(
                    SettingsScreenEvent.LogoutClicked,
                    SettingsScreenEvent.AppInfoClicked,
                    SettingsScreenEvent.DarkModeChanged(isEnabled = true),
                ),
                events,
            )
        }
    }

    private fun setSettingsContent(onEvent: (SettingsScreenEvent) -> Unit) {
        composeRule.setContent {
            HopesTheme {
                SettingsScreen(
                    isDarkModeEnabled = false,
                    isNotificationEnabled = true,
                    isDeleteAccountEnabled = true,
                    appVersion = "1.5",
                    onEvent = onEvent,
                    onBackClick = {},
                )
            }
        }
    }
}
