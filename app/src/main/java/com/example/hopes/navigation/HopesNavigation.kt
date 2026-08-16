package com.example.hopes.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.chat.presentation.ChatRoute
import com.example.hopes.feature.chat.presentation.detail.ChatDetailRoute
import com.example.hopes.feature.detail.presentation.DetailRoute
import com.example.hopes.feature.detail.presentation.DetailScreenType
import com.example.hopes.feature.detail.presentation.MyPageRoute
import com.example.hopes.feature.history.presentation.HistoryRoute
import com.example.hopes.feature.home.presentation.HomeRoute
import com.example.hopes.feature.settings.presentation.SettingsRoute

private const val AUTH_ROUTE = "auth"

/** 인증·탭·설정 및 서버 채팅 상세의 단일 탐색 그래프다. */
@Composable
fun HopesNavigation(
    isDarkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    val hopesNavController = rememberNavController()

    NavHost(
        navController = hopesNavController,
        startDestination = AUTH_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(AUTH_ROUTE) {
            AuthRoute(onAuthenticated = {
                hopesNavController.navigate(HopesDestination.Home.route) { popUpTo(AUTH_ROUTE) { inclusive = true } }
            })
        }
        composable(HopesDestination.Home.route) { HomeRoute(onNavigate = hopesNavController::navigateToTopLevel) }
        composable(
            route = chatRoutePattern(),
            arguments = listOf(
                navArgument(CHAT_NEW_CHAT_ARGUMENT) {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) { backStackEntry ->
            ChatRoute(
                onNavigate = hopesNavController::navigateToTopLevel,
                onNavigateToChatDetail = { chatId -> hopesNavController.navigate(chatDetailRoute(chatId)) },
                isNewChatRequested = backStackEntry.arguments?.getBoolean(CHAT_NEW_CHAT_ARGUMENT) ?: false,
            )
        }
        composable(HopesDestination.History.route) {
            HistoryRoute(
                onNavigate = hopesNavController::navigateToTopLevel,
                onNavigateToChatDetail = { chatId -> hopesNavController.navigate(chatDetailRoute(chatId)) },
                onStartNewChat = hopesNavController::navigateToNewChat,
            )
        }
        composable(HopesDestination.Settings.route) {
            MyPageRoute(onNavigate = hopesNavController::navigateToTopLevel)
        }
        composable(HopesDestination.AppSettings.route) {
            SettingsRoute(hopesNavController::navigateToTopLevel, isDarkThemeEnabled, onDarkThemeChange, hopesNavController::popBackStack, { hopesNavController.navigate(HopesDestination.PersonalSettings.route) }, { hopesNavController.navigate(HopesDestination.Contact.route) }, { hopesNavController.navigate(AUTH_ROUTE) { popUpTo(0) { inclusive = true } } })
        }
        composable(HopesDestination.PersonalSettings.route) {
            DetailRoute(
                screenType = DetailScreenType.PersonalSettings,
                onBackClick = hopesNavController::popBackStack,
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
        composable(HopesDestination.Contact.route) {
            DetailRoute(
                screenType = DetailScreenType.Contact,
                onBackClick = hopesNavController::popBackStack,
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
        composable(HopesDestination.ChatDetail.route, arguments = listOf(navArgument(CHAT_DETAIL_ARGUMENT) { type = NavType.LongType })) {
            ChatDetailRoute(onBackClick = hopesNavController::popBackStack, onNavigate = hopesNavController::navigateToTopLevel)
        }
    }
}

private fun NavHostController.navigateToTopLevel(destination: HopesDestination) {
    navigate(destination.route) {
        launchSingleTop = true
        popUpTo(HopesDestination.Home.route) { saveState = true }
        restoreState = true
    }
}

/** 기록 화면의 새 대화 시작에서 빈 채팅 입력 상태를 보장하는 탐색이다. */
private fun NavHostController.navigateToNewChat() {
    navigate(chatRoute(isNewChatRequested = true)) {
        launchSingleTop = true
        popUpTo(HopesDestination.Home.route) { saveState = true }
    }
}
