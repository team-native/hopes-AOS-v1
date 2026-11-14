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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HistoryScreenContentTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val today = LocalDate.of(2026, 11, 14)
    private val events = mutableListOf<HistoryScreenEvent>()

    private fun setContent(uiState: HistoryUiState) {
        composeRule.setContent {
            HopesTheme {
                HistoryScreenContent(
                    uiState = uiState,
                    onEvent = { events += it },
                    onNavigate = {},
                    today = today,
                )
            }
        }
    }

    @Test
    fun content_groupsChatsIntoLastSevenDaysAndEarlier() {
        setContent(
            HistoryUiState(
                chats =
                    listOf(
                        ChatSummaryUiModel(id = 1, title = "기숙사 하루 일과가 어떻게 돼?", date = today.minusDays(1)),
                        ChatSummaryUiModel(id = 2, title = "입학하려면 뭘 준비해야 해?", date = today.minusDays(30)),
                    ),
                contentState = HistoryContentState.Content,
            ),
        )

        composeRule.onNodeWithText("지난 대화").assertIsDisplayed()
        composeRule.onNodeWithText("지난 7일").assertIsDisplayed()
        composeRule.onNodeWithText("이전").assertIsDisplayed()
        composeRule.onNodeWithText("어제").assertIsDisplayed()
        composeRule.onNodeWithText("입학하려면 뭘 준비해야 해?").assertIsDisplayed()
    }

    @Test
    fun content_emitsNewChatSearchAndChatEvents() {
        setContent(
            HistoryUiState(
                chats = listOf(ChatSummaryUiModel(id = 42, title = "대화 제목", date = today)),
                contentState = HistoryContentState.Content,
            ),
        )

        composeRule.onNodeWithText("새 대화 시작").performClick()
        composeRule.onNodeWithContentDescription("지난 대화 검색").performTextInput("전공")
        composeRule.onNodeWithText("대화 제목").performClick()

        composeRule.runOnIdle {
            assertEquals(HistoryScreenEvent.NewChatClicked, events[0])
            assertTrue(events.contains(HistoryScreenEvent.SearchQueryChanged("전공")))
            assertEquals(HistoryScreenEvent.ChatClicked(42L), events.last())
        }
    }

    @Test
    fun searchResults_showCount() {
        setContent(
            HistoryUiState(
                searchQuery = "기숙사",
                chats = listOf(ChatSummaryUiModel(id = 1, title = "기숙사 방 배정은 어떻게 돼?", date = today)),
                contentState = HistoryContentState.Content,
            ),
        )

        composeRule.onNodeWithText("검색 결과 1개").assertIsDisplayed()
    }

    @Test
    fun emptyAndNoResultStates_showMatchingMessages() {
        setContent(HistoryUiState(contentState = HistoryContentState.Empty))

        composeRule.onNodeWithText("아직 대화가 없어요").assertIsDisplayed()
        composeRule.onNodeWithText("채팅 시작하기").performClick()

        composeRule.runOnIdle { assertEquals(listOf(HistoryScreenEvent.NewChatClicked), events) }
    }

    @Test
    fun pendingDelete_showsConfirmDialog() {
        setContent(
            HistoryUiState(
                chats = listOf(ChatSummaryUiModel(id = 7, title = "지울 대화", date = today)),
                contentState = HistoryContentState.Content,
                pendingDeleteChatId = 7,
            ),
        )

        composeRule.onNodeWithText("이 대화를 삭제할까요?").assertIsDisplayed()
        composeRule.onNodeWithText("삭제").performClick()

        composeRule.runOnIdle { assertEquals(listOf(HistoryScreenEvent.DeleteChatConfirmed), events) }
    }
}
