package com.example.hopes.core.designsystem.component

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.home.presentation.HomeUiState
import com.example.hopes.feature.home.presentation.content.HomeScreenContent
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HopesScaffoldTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun bottomNavigation_displaysAllTopLevelDestinationsAndNavigates() {
        var selectedDestination by mutableStateOf<HopesDestination>(HopesDestination.Home)

        composeRule.setContent {
            HopesTheme {
                HopesScaffold(
                    selectedDestination = selectedDestination,
                    onNavigate = { destination ->
                        selectedDestination = destination
                    },
                ) {
                }
            }
        }

        composeRule.onNodeWithText("홈").assertIsDisplayed()
        composeRule.onNodeWithText("채팅").assertIsDisplayed()
        composeRule.onNodeWithText("커뮤니티").assertIsDisplayed()
        composeRule.onNodeWithText("기록").assertIsDisplayed()
        composeRule.onNodeWithText("마이페이지").assertIsDisplayed()

        composeRule.onNodeWithText("채팅").performClick()

        composeRule.runOnIdle {
            assertEquals(HopesDestination.Chat(), selectedDestination)
        }
    }

    @Test
    fun homeStartChatButton_isReachableOnShortScreen() {
        composeRule.setContent {
            HopesTheme {
                Box(
                    modifier =
                        Modifier.size(
                            width = 402.dp,
                            height = 360.dp,
                        ),
                ) {
                    HomeScreenContent(
                        uiState = HomeUiState(userName = "서하"),
                        onEvent = {},
                        onNavigate = {},
                    )
                }
            }
        }

        composeRule
            .onNodeWithTag("home_start_chat_button")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule
            .onNodeWithTag("home_tip_card_2")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
