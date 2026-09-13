package com.example.hopes.feature.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.settings.presentation.content.SettingsScreenContent
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsResponsiveTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun settingsRowsAndAccountSection_useTheSameHorizontalPadding() {
        composeRule.setContent {
            HopesTheme {
                Box(
                    modifier = Modifier
                        .width(402.dp)
                        .fillMaxHeight(),
                ) {
                    SettingsScreenContent(
                        onNavigate = {},
                        isDarkModeEnabled = false,
                        onDarkModeChange = {},
                        onBackClick = {},
                        onNavigateToPersonalSettings = {},
                        onNavigateToContact = {},
                        onNavigateToPrivacyPolicy = {},
                        uiState = SettingsUiState(),
                        onEvent = {},
                    )
                }
            }
        }

        val settingsOptionRows = composeRule
            .onAllNodesWithTag("settings_option_row")
            .assertCountEquals(3)
        val expectedWidth = settingsOptionRows
            .get(0)
            .fetchSemanticsNode()
            .boundsInRoot
            .width

        assertEquals(
            expectedWidth,
            settingsOptionRows.get(1).fetchSemanticsNode().boundsInRoot.width,
            0.5f,
        )
        assertEquals(
            expectedWidth,
            settingsOptionRows.get(2).fetchSemanticsNode().boundsInRoot.width,
            0.5f,
        )
        assertEquals(
            expectedWidth,
            composeRule.onNodeWithTag("settings_account_section")
                .fetchSemanticsNode()
                .boundsInRoot
                .width,
            0.5f,
        )
    }
}
