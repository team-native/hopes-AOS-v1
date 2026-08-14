package com.example.hopes.feature.history.presentation.content

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.hopes.feature.history.presentation.ChatSummaryUiModel
import com.example.hopes.feature.history.presentation.HistoryContentState
import com.example.hopes.feature.history.presentation.HistoryScreenEvent
import com.example.hopes.feature.history.presentation.HistoryUiState
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HistoryScreenContentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun content_displaysNewChatSearchAndUngroupedConversations() {
        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = HistoryUiState(
                        chats = listOf(
                            ChatSummaryUiModel(id = 1L, title = "기숙사 하루 일과가 어떻게 돼?"),
                            ChatSummaryUiModel(id = 2L, title = "전공 선택은 어떻게 하는 게 좋아?"),
                        ),
                        contentState = HistoryContentState.Content,
                    ),
                    onEvent = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("지난 대화").assertIsDisplayed()
        composeRule.onNodeWithText("+  새 대화 시작").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("지난 대화 검색").assertIsDisplayed()
        composeRule.onNodeWithText("기숙사 하루 일과가 어떻게 돼?").assertIsDisplayed()
        composeRule.onNodeWithText("전공 선택은 어떻게 하는 게 좋아?").assertIsDisplayed()
    }

    @Test
    fun content_emitsNewChatSearchAndConversationEvents() {
        var emittedEvent: HistoryScreenEvent? = null

        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = HistoryUiState(
                        chats = listOf(
                            ChatSummaryUiModel(id = 42L, title = "대화 제목"),
                        ),
                        contentState = HistoryContentState.Content,
                    ),
                    onEvent = { event ->
                        emittedEvent = event
                    },
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("+  새 대화 시작").performClick()

        composeRule.runOnIdle {
            assertEquals(HistoryScreenEvent.NewChatClicked, emittedEvent)
        }

        composeRule.onNodeWithContentDescription("지난 대화 검색")
            .performClick()
            .performTextInput("전공")

        composeRule.runOnIdle {
            assertEquals(HistoryScreenEvent.SearchQueryChanged("전공"), emittedEvent)
        }

        composeRule.onNodeWithText("대화 제목").performClick()

        composeRule.runOnIdle {
            assertEquals(HistoryScreenEvent.ChatClicked(42L), emittedEvent)
        }
    }

    @Test
    fun content_displaysLoadingEmptyAndErrorStates() {
        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = HistoryUiState(
                        contentState = HistoryContentState.Loading,
                    ),
                    onEvent = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("지난 대화를 불러오는 중이에요.").assertIsDisplayed()

        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = HistoryUiState(
                        contentState = HistoryContentState.Empty,
                    ),
                    onEvent = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("지난 대화가 없어요.").assertIsDisplayed()

        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = HistoryUiState(
                        contentState = HistoryContentState.Error,
                    ),
                    onEvent = {},
                    onNavigate = {},
                )
            }
        }

        composeRule.onNodeWithText("지난 대화를 불러오지 못했어요. 다시 시도해주세요.").assertIsDisplayed()
    }
}
