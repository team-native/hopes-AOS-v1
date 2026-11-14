package com.example.hopes.feature.chat.detail.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailUiState
import com.example.hopes.feature.chat.detail.viewmodel.ChatMessageUiModel
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatDetailScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun autoSavedChat_doesNotDisplaySaveAction() {
        setDetailContent(
            uiState = ChatDetailUiState(title = "기숙사 생활", isLoading = false),
            onEvent = {},
        )

        composeRule.onNodeWithText("선배 답변 · 실제 경험 기반").assertIsDisplayed()
        composeRule.onAllNodesWithText("저장").assertCountEquals(0)
    }

    @Test
    fun failedAnswer_keepsQuestionAndResendsOnRetry() {
        val events = mutableListOf<ChatDetailScreenEvent>()
        setDetailContent(
            uiState =
                ChatDetailUiState(
                    title = "전공 공부",
                    isLoading = false,
                    messages = listOf(ChatMessageUiModel(id = null, content = "프론트엔드는 뭐부터 공부하면 좋아?", isUser = true)),
                    failedQuestion = "프론트엔드는 뭐부터 공부하면 좋아?",
                ),
            onEvent = { events += it },
        )

        composeRule.onNodeWithText("프론트엔드는 뭐부터 공부하면 좋아?").assertIsDisplayed()
        composeRule.onNodeWithText("답변을 만들지 못했어요").assertIsDisplayed()
        composeRule.onNodeWithText("다시 시도").performClick()

        composeRule.runOnIdle {
            assertEquals(listOf(ChatDetailScreenEvent.ResendClicked), events)
        }
    }

    private fun setDetailContent(
        uiState: ChatDetailUiState,
        onEvent: (ChatDetailScreenEvent) -> Unit,
    ) {
        composeRule.setContent {
            HopesTheme {
                ChatDetailScreen(
                    uiState = uiState,
                    subtitle = "선배 답변 · 실제 경험 기반",
                    onEvent = onEvent,
                    onCopyAnswer = {},
                    onShareAnswer = {},
                    onCopyCode = {},
                )
            }
        }
    }
}
