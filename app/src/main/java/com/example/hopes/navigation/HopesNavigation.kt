package com.example.hopes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hopes.feature.chat.presentation.ChatRoute
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.history.presentation.HistoryRoute
import com.example.hopes.feature.home.presentation.HomeRoute
import com.example.hopes.feature.settings.presentation.SettingsRoute

/** UI 데모의 네 개 화면을 연결한다. */
@Composable
fun HopesNavigation() {
    val hopesNavController = rememberNavController()

    NavHost(
        navController = hopesNavController,
        startDestination = "auth",
    ) {
        composable("auth") {
            AuthRoute(
                onAuthenticated = {
                    hopesNavController.navigate(HopesDestination.Home.route) {
                        popUpTo("auth") { inclusive = true }
                    }
                },
            )
        }
        composable(HopesDestination.Home.route) {
            HomeRoute(onNavigate = hopesNavController::navigateTo)
        }
        composable(HopesDestination.Chat.route) {
            ChatRoute(onNavigate = hopesNavController::navigateTo)
        }
        composable(HopesDestination.History.route) {
            HistoryRoute(
                onNavigate = hopesNavController::navigateTo,
                onNavigateToChatDetail = { hopesNavController.navigate(HopesDestination.ChatDetail.route) },
            )
        }
        composable(HopesDestination.Settings.route) {
            SettingsRoute(
                onNavigate = hopesNavController::navigateTo,
                onNavigateToMyPage = { hopesNavController.navigate(HopesDestination.MyPage.route) },
                onNavigateToPersonalSettings = { hopesNavController.navigate(HopesDestination.PersonalSettings.route) },
                onNavigateToContact = { hopesNavController.navigate(HopesDestination.Contact.route) },
            )
        }
        composable(HopesDestination.MyPage.route) { DemoDetailScreen(destination = HopesDestination.MyPage, onBackClick = hopesNavController::popBackStack) }
        composable(HopesDestination.PersonalSettings.route) { DemoDetailScreen(destination = HopesDestination.PersonalSettings, onBackClick = hopesNavController::popBackStack) }
        composable(HopesDestination.Contact.route) { DemoDetailScreen(destination = HopesDestination.Contact, onBackClick = hopesNavController::popBackStack) }
        composable(HopesDestination.ChatDetail.route) { DemoDetailScreen(destination = HopesDestination.ChatDetail, onBackClick = hopesNavController::popBackStack) }
    }
}

private fun NavHostController.navigateTo(destination: HopesDestination) {
    navigate(destination.route) {
        launchSingleTop = true
        popUpTo(HopesDestination.Home.route) {
            saveState = true
        }
        restoreState = true
    }
}
