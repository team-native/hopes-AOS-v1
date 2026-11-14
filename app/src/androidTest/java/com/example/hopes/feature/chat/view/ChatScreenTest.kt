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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.example.hopes.feature.chat.viewmodel.ChatScreenEvent
import com.example.hopes.feature.chat.viewmodel.ChatUiState
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun composer_displaysSingleSendActionAndFitsNarrowScreen() {
        composeRule.setContent {
            HopesTheme {
                Box(modifier = Modifier.width(360.dp).fillMaxHeight()) {
                    ChatScreen(
                        uiState = ChatUiState(),
                        onEvent = {},
                        onDepartmentClick = {},
                        onMajorClick = {},
                        onNavigate = {},
                    )
                }
            }
        }

        composeRule.onAllNodesWithContentDescription("전송").assertCountEquals(1)
        composeRule
            .onNodeWithTag("chat_composer_surface")
            .assertLeftPositionInRootIsEqualTo(AppSpacing.Medium)
            .assertWidthIsEqualTo(360.dp - (AppSpacing.Medium * 2))
    }

    @Test
    fun unselectedContext_showsDefaultQuestionsAndSelectHint() {
        composeRule.setContent {
            HopesTheme {
                ChatScreen(
                    uiState = ChatUiState(),
                    onEvent = {},
                    onDepartmentClick = {},
                    onMajorClick = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("학과 선택").assertIsDisplayed()
        composeRule.onNodeWithText("기본 추천 질문").assertIsDisplayed()
        composeRule.onNodeWithText("학과와 전공을 고르면 나에게 맞는 질문을 추천해 드려요.").assertIsDisplayed()
    }

    @Test
    fun personalizedQuestion_clickFillsInputThroughEvent() {
        val events = mutableListOf<ChatScreenEvent>()
        composeRule.setContent {
            HopesTheme {
                ChatScreen(
                    uiState = ChatUiState(department = ChatDepartment.Software, major = ChatMajor.Frontend),
                    onEvent = { events += it },
                    onDepartmentClick = {},
                    onMajorClick = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("소프트웨어개발과 · Frontend 추천 질문").assertIsDisplayed()
        composeRule.onNodeWithText("Frontend는 뭐부터 공부하면 좋아?").performClick()

        composeRule.runOnIdle {
            assertEquals(listOf(ChatScreenEvent.SuggestionClicked("Frontend는 뭐부터 공부하면 좋아?")), events)
        }
    }
}
