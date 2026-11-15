package com.example.hopes.feature.chat.detail.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.feature.chat.detail.view.component.ChatLocationCard
import com.example.hopes.feature.chat.detail.viewmodel.ChatLocationUiState
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatLocationCardTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val clicks = mutableListOf<String>()

    private fun setCard(state: ChatLocationUiState) {
        composeRule.setContent {
            HopesTheme {
                ChatLocationCard(
                    state = state,
                    onAllowClick = { clicks += "allow" },
                    onLaterClick = { clicks += "later" },
                    onRetryClick = { clicks += "retry" },
                    onOpenSettingsClick = { clicks += "settings" },
                    onViewMapClick = { clicks += "map" },
                )
            }
        }
    }

    @Test
    fun permissionCard_sendsAllowAndLater() {
        setCard(ChatLocationUiState.PermissionRequired)

        composeRule.onNodeWithText("위치 권한이 필요해요").assertIsDisplayed()
        composeRule.onNodeWithText("나중에").performClick()
        composeRule.onNodeWithText("위치 허용").performClick()

        composeRule.runOnIdle { assertEquals(listOf("later", "allow"), clicks) }
    }

    @Test
    fun nearBuilding_showsPlaceAndOpensMap() {
        setCard(ChatLocationUiState.NearBuilding(buildingName = "본관", accuracyMeters = 15))

        composeRule.onNodeWithText("지금 학교 본관 근처에 있어요.").assertIsDisplayed()
        composeRule.onNodeWithText("정확도 약 ±15m · 방금 측정").assertIsDisplayed()
        composeRule.onNodeWithText("지도 보기").performClick()

        composeRule.runOnIdle { assertEquals(listOf("map"), clicks) }
    }

    @Test
    fun failed_offersSettingsAndRetry() {
        setCard(ChatLocationUiState.Failed)

        composeRule.onNodeWithText("위치를 확인하지 못했어요").assertIsDisplayed()
        composeRule.onNodeWithText("설정 열기").performClick()
        composeRule.onNodeWithText("다시 시도").performClick()

        composeRule.runOnIdle { assertEquals(listOf("settings", "retry"), clicks) }
    }
}
