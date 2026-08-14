package com.example.hopes.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailScreenType
import com.example.hopes.feature.detail.presentation.DetailUiState
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
    var profileName by rememberSaveable { mutableStateOf("") }
    var profileIntroduction by rememberSaveable { mutableStateOf("") }
    var isProfileSaved by rememberSaveable { mutableStateOf(false) }
    var personalPrompt by rememberSaveable { mutableStateOf("") }
    var isPromptSaved by rememberSaveable { mutableStateOf(false) }
    var contactEmail by rememberSaveable { mutableStateOf("") }
    var contactMessage by rememberSaveable { mutableStateOf("") }
    var isContactSent by rememberSaveable { mutableStateOf(false) }

    fun detailUiState(): DetailUiState = DetailUiState(
        profileName = profileName,
        profileIntroduction = profileIntroduction,
        isProfileSaved = isProfileSaved,
        personalPrompt = personalPrompt,
        isPromptSaved = isPromptSaved,
        contactEmail = contactEmail,
        contactMessage = contactMessage,
        isContactSent = isContactSent,
    )

    fun handleDetailEvent(event: DetailScreenEvent) {
        when (event) {
            DetailScreenEvent.BackClicked -> hopesNavController.popBackStack()
            DetailScreenEvent.AppSettingsClicked -> hopesNavController.navigate(HopesDestination.AppSettings.route)
            is DetailScreenEvent.ProfileNameChanged -> { profileName = event.value; isProfileSaved = false }
            is DetailScreenEvent.ProfileIntroductionChanged -> { profileIntroduction = event.value; isProfileSaved = false }
            DetailScreenEvent.ProfileSaveClicked -> isProfileSaved = true
            is DetailScreenEvent.PersonalPromptChanged -> { personalPrompt = event.value; isPromptSaved = false }
            DetailScreenEvent.PersonalPromptSaveClicked -> isPromptSaved = true
            is DetailScreenEvent.ContactEmailChanged -> { contactEmail = event.value; isContactSent = false }
            is DetailScreenEvent.ContactMessageChanged -> { contactMessage = event.value; isContactSent = false }
            DetailScreenEvent.ContactSendClicked -> isContactSent = contactEmail.isNotBlank() && contactMessage.isNotBlank()
        }
    }

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
        composable(HopesDestination.Chat.route) {
            ChatRoute(onNavigate = hopesNavController::navigateToTopLevel, onNavigateToChatDetail = { chatId -> hopesNavController.navigate(chatDetailRoute(chatId)) })
        }
        composable(HopesDestination.History.route) {
            HistoryRoute(onNavigate = hopesNavController::navigateToTopLevel, onNavigateToChatDetail = { chatId -> hopesNavController.navigate(chatDetailRoute(chatId)) })
        }
        composable(HopesDestination.Settings.route) { DetailRoute(DetailScreenType.MyPage, detailUiState(), ::handleDetailEvent, hopesNavController::navigateToTopLevel) }
        composable(HopesDestination.AppSettings.route) {
            SettingsRoute(hopesNavController::navigateToTopLevel, isDarkThemeEnabled, onDarkThemeChange, hopesNavController::popBackStack, { hopesNavController.navigate(HopesDestination.PersonalSettings.route) }, { hopesNavController.navigate(HopesDestination.Contact.route) }, { hopesNavController.navigate(AUTH_ROUTE) { popUpTo(0) { inclusive = true } } })
        }
        composable(HopesDestination.PersonalSettings.route) { DetailRoute(DetailScreenType.PersonalSettings, detailUiState(), ::handleDetailEvent, hopesNavController::navigateToTopLevel) }
        composable(HopesDestination.Contact.route) { DetailRoute(DetailScreenType.Contact, detailUiState(), ::handleDetailEvent, hopesNavController::navigateToTopLevel) }
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
