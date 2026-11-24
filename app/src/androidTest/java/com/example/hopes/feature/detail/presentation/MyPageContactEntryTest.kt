package com.example.hopes.feature.detail.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.navigation.HopesDestination
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MyPageContactEntryTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun inquiryEntryRemainsPrimaryAndNoPlaceholderPhoneActionIsShown() {
        val events = mutableListOf<MyPageScreenEvent>()
        composeRule.setContent {
            HopesTheme {
                MyPageScreen(
                    uiState = MyPageUiState(isProfileLoading = false),
                    toastMessage = null,
                    onToastDismiss = {},
                    onEvent = events::add,
                    onNavigate = { _: HopesDestination -> },
                )
            }
        }

        composeRule.onNodeWithText("문의하기").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("0000-0000").assertDoesNotExist()
        composeRule.onNodeWithText("전화하기").assertDoesNotExist()
        composeRule.runOnIdle {
            assertEquals(listOf(MyPageScreenEvent.ContactClicked), events)
        }
    }
}
