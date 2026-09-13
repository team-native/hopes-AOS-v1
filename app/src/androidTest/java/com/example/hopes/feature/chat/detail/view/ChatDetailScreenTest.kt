package com.example.hopes.feature.chat.detail.view

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailScreenEvent
import com.example.hopes.feature.chat.detail.viewmodel.ChatDetailUiState
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Rule
import org.junit.Test

class ChatDetailScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun autoSavedChat_doesNotDisplaySaveAction() {
        composeRule.setContent {
            HopesTheme {
                Box(
                    modifier = Modifier
                        .width(402.dp)
                        .fillMaxHeight(),
                ) {
                    ChatDetailScreen(
                        uiState = ChatDetailUiState(
                            title = "기숙사 생활",
                            isLoading = false,
                        ),
                        onEvent = { _: ChatDetailScreenEvent -> },
                        onNavigate = {},
                    )
                }
            }
        }

        composeRule
            .onNodeWithText("선배 답변 · 실제 경험 기반")
            .assertIsDisplayed()
        composeRule.onAllNodesWithText("저장").assertCountEquals(0)
    }
}
