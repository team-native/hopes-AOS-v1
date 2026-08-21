package com.example.hopes.feature.chat.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun composer_displaysSingleSendActionAboveBottomNavigation() {
        composeRule.setContent {
            HopesTheme {
                ChatScreen(
                    questionText = "",
                    onEvent = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onAllNodesWithContentDescription("전송").assertCountEquals(1)
        composeRule.onNodeWithText("채팅").assertIsDisplayed()
    }
}
