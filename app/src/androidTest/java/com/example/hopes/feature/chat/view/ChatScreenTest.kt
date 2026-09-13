package com.example.hopes.feature.chat.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
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

    @Test
    fun composer_andWelcomeLogo_fitNarrowScreenWithHorizontalPadding() {
        composeRule.setContent {
            HopesTheme {
                Box(
                    modifier = Modifier
                        .width(360.dp)
                        .fillMaxHeight(),
                ) {
                    ChatScreen(
                        questionText = "",
                        onEvent = {},
                        onNavigate = {},
                    )
                }
            }
        }

        composeRule
            .onNodeWithTag("chat_composer_surface")
            .assertLeftPositionInRootIsEqualTo(AppSpacing.ScreenHorizontal)
            .assertWidthIsEqualTo(360.dp - (AppSpacing.ScreenHorizontal * 2))

        composeRule
            .onNodeWithTag("chat_welcome_logo_mark")
            .assertWidthIsEqualTo(60.dp)
    }
}
