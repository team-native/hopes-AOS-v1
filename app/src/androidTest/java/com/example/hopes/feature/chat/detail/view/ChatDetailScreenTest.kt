package com.example.hopes.feature.chat.detail.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentKind
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentUiModel
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

    @Test
    fun generatingAnswer_showsStopButtonThatSendsStop() {
        val events = mutableListOf<ChatDetailScreenEvent>()
        setDetailContent(
            uiState =
                ChatDetailUiState(
                    title = "전공 공부",
                    isLoading = false,
                    messages = listOf(ChatMessageUiModel(id = null, content = "질문", isUser = true)),
                    isSending = true,
                ),
            onEvent = { events += it },
        )

        composeRule.onNodeWithText("답변 중지").performClick()

        composeRule.runOnIdle {
            assertEquals(listOf(ChatDetailScreenEvent.StopClicked), events)
        }
    }

    @Test
    fun offline_showsBannerPendingStatusAndOfflinePlaceholder() {
        setDetailContent(
            uiState =
                ChatDetailUiState(
                    title = "전공 공부",
                    isLoading = false,
                    messages = listOf(ChatMessageUiModel(id = null, content = "기숙사 외출은 어떻게 해?", isUser = true)),
                    pendingQuestion = "기숙사 외출은 어떻게 해?",
                    isOffline = true,
                ),
            onEvent = {},
        )

        composeRule.onNodeWithText("인터넷 연결이 끊겼어요. 연결되면 자동으로 다시 시도해요.").assertIsDisplayed()
        composeRule.onNodeWithText("연결되면 전송돼요").assertIsDisplayed()
        composeRule.onNodeWithText("오프라인 상태예요").assertIsDisplayed()
    }

    @Test
    fun newChatHeaderAction_asksForConfirmation() {
        var isNewChatConfirmed = false
        composeRule.setContent {
            HopesTheme {
                ChatDetailScreen(
                    uiState = ChatDetailUiState(title = "전공 공부", isLoading = false),
                    subtitle = "선배 답변 · 실제 경험 기반",
                    onEvent = {},
                    onCopyAnswer = {},
                    onShareAnswer = {},
                    onCopyCode = {},
                    onNewChatConfirm = { isNewChatConfirmed = true },
                )
            }
        }

        composeRule.onNodeWithText("새 대화").performClick()
        composeRule.onNodeWithText("새 대화를 시작할까요?").assertIsDisplayed()
        composeRule.onAllNodesWithText("새 대화")[1].performClick()

        composeRule.runOnIdle { assertEquals(true, isNewChatConfirmed) }
    }

    @Test
    fun draftAttachments_showCountAndRemoveSendsEvent() {
        val events = mutableListOf<ChatDetailScreenEvent>()
        setDetailContent(
            uiState =
                ChatDetailUiState(
                    title = "기숙사 생활",
                    isLoading = false,
                    draftAttachments =
                        listOf(
                            ChatAttachmentUiModel(
                                uri = "content://pdf/1",
                                kind = ChatAttachmentKind.Pdf,
                                name = "생활수칙.pdf",
                                pageCount = 12,
                            ),
                        ),
                ),
            onEvent = { events += it },
        )

        composeRule.onNodeWithText("첨부 1개").assertIsDisplayed()
        composeRule.onNodeWithText("PDF · 12쪽").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("생활수칙.pdf 첨부 빼기").performClick()

        composeRule.runOnIdle {
            assertEquals(listOf(ChatDetailScreenEvent.AttachmentRemoved("content://pdf/1")), events)
        }
    }

    @Test
    fun questionWithAttachments_showsAnalyzingSummaryWhileSending() {
        setDetailContent(
            uiState =
                ChatDetailUiState(
                    title = "기숙사 생활",
                    isLoading = false,
                    messages =
                        listOf(
                            ChatMessageUiModel(
                                id = null,
                                content = "이 자료 요약해줘",
                                isUser = true,
                                attachments =
                                    listOf(
                                        ChatAttachmentUiModel("content://image/1", ChatAttachmentKind.Image, "a.jpg"),
                                        ChatAttachmentUiModel("content://image/2", ChatAttachmentKind.Image, "b.jpg"),
                                        ChatAttachmentUiModel("content://pdf/1", ChatAttachmentKind.Pdf, "생활수칙.pdf"),
                                    ),
                            ),
                        ),
                    isSending = true,
                ),
            onEvent = {},
        )

        composeRule.onNodeWithText("자료를 분석하고 있어요…").assertIsDisplayed()
        composeRule.onNodeWithText("업로드 완료 · 이미지 2장, PDF 1개").assertIsDisplayed()
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
