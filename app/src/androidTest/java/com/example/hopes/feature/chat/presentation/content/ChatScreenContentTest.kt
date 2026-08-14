package com.example.hopes.feature.chat.presentation.content

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Rule
import org.junit.Test

class ChatScreenContentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun composer_displaysSingleSendActionAboveBottomNavigation() {
        composeRule.setContent {
            HopesTheme {
                ChatScreenContent(
                    questionText = "",
                    isCreateChatError = false,
                    onQuestionChange = {},
                    onSubmitClick = {},
                    onNewChatClick = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onAllNodesWithContentDescription("전송").assertCountEquals(1)
        composeRule.onNodeWithText("채팅").assertIsDisplayed()
    }

    @Test
    fun createChatError_displaysRetryMessage() {
        composeRule.setContent {
            HopesTheme {
                ChatScreenContent(
                    questionText = "질문",
                    isCreateChatError = true,
                    onQuestionChange = {},
                    onSubmitClick = {},
                    onNewChatClick = {},
                    onNavigate = {},
                )
            }
        }

        composeRule
            .onNodeWithText("채팅을 시작하지 못했어요. 잠시 후 다시 시도해 주세요.")
            .assertIsDisplayed()
    }
}
