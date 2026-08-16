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
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
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
        var selectedDestination by mutableStateOf(HopesDestination.Home)

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
        composeRule.onNodeWithText("기록").assertIsDisplayed()
        composeRule.onNodeWithText("설정").assertIsDisplayed()

        composeRule.onNodeWithText("채팅").performClick()

        composeRule.runOnIdle {
            assertEquals(HopesDestination.Chat, selectedDestination)
        }
    }

    @Test
    fun homeStartChatButton_isReachableOnShortScreen() {
        composeRule.setContent {
            HopesTheme {
                Box(
                    modifier = Modifier.size(
                        width = 402.dp,
                        height = 360.dp,
                    ),
                ) {
                    HomeScreenContent(
                        onStartChatClick = {},
                        onNavigate = {},
                    )
                }
            }
        }

        composeRule
            .onNodeWithText("채팅 시작하기")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
