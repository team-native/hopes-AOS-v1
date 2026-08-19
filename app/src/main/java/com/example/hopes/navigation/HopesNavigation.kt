package com.example.hopes.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hopes.core.session.SessionState
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.auth.presentation.SessionViewModel
import com.example.hopes.feature.chat.presentation.ChatRoute
import com.example.hopes.feature.chat.presentation.detail.ChatDetailRoute
import com.example.hopes.feature.detail.presentation.DetailRoute
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailScreenType
import com.example.hopes.feature.detail.presentation.DetailUiState
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
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val hopesNavController = rememberNavController()
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()
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

    if (sessionState is SessionState.Loading) {
        SessionRestoringIndicator()
        return
    }

    key(sessionState) {
        NavHost(
            navController = hopesNavController,
            startDestination = sessionState.startDestination(),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable(AUTH_ROUTE) {
                AuthRoute(onAuthenticated = {
                    hopesNavController.navigate(HopesDestination.Home.route) {
                        popUpTo(AUTH_ROUTE) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(HopesDestination.Home.route) {
                HomeRoute(onNavigate = hopesNavController::navigateToTopLevel)
            }
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
                    onNavigateToChatDetail = { chatId ->
                        hopesNavController.navigate(chatDetailRoute(chatId))
                    },
                    isNewChatRequested = backStackEntry.arguments
                        ?.getBoolean(CHAT_NEW_CHAT_ARGUMENT)
                        ?: false,
                )
            }
            composable(HopesDestination.History.route) {
                HistoryRoute(
                    onNavigate = hopesNavController::navigateToTopLevel,
                    onNavigateToChatDetail = { chatId ->
                        hopesNavController.navigate(chatDetailRoute(chatId))
                    },
                    onStartNewChat = hopesNavController::navigateToNewChat,
                )
            }
            composable(HopesDestination.Settings.route) {
                MyPageRoute(
                    onNavigate = hopesNavController::navigateToTopLevel,
                    // 설정은 마이페이지 하위 화면이라 top-level 탐색이 아닌 일반 push로 진입시켜,
                    // 뒤로가기(popBackStack) 시 홈이 아닌 마이페이지로 돌아오게 한다.
                    onNavigateToAppSettings = {
                        hopesNavController.navigate(HopesDestination.AppSettings.route)
                    },
                )
            }
            composable(HopesDestination.AppSettings.route) {
                SettingsRoute(
                    onNavigate = hopesNavController::navigateToTopLevel,
                    isDarkModeEnabled = isDarkThemeEnabled,
                    onDarkModeChange = onDarkThemeChange,
                    onBackClick = hopesNavController::popBackStack,
                    onNavigateToPersonalSettings = {
                        hopesNavController.navigate(HopesDestination.PersonalSettings.route)
                    },
                    onNavigateToContact = {
                        hopesNavController.navigate(HopesDestination.Contact.route)
                    },
                    onLogout = {
                        hopesNavController.navigate(AUTH_ROUTE) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
            composable(HopesDestination.PersonalSettings.route) {
                DetailRoute(
                    DetailScreenType.PersonalSettings,
                    detailUiState(),
                    ::handleDetailEvent,
                    hopesNavController::navigateToTopLevel,
                )
            }
            composable(HopesDestination.Contact.route) {
                DetailRoute(
                    DetailScreenType.Contact,
                    detailUiState(),
                    ::handleDetailEvent,
                    hopesNavController::navigateToTopLevel,
                )
            }
            composable(
                HopesDestination.ChatDetail.route,
                arguments = listOf(
                    navArgument(CHAT_DETAIL_ARGUMENT) {
                        type = NavType.LongType
                    },
                ),
            ) {
                ChatDetailRoute(
                    onBackClick = hopesNavController::popBackStack,
                    onNavigate = hopesNavController::navigateToTopLevel,
                )
            }
        }
    }
}

/** 저장 세션을 읽는 동안 인증 화면이 잠시 노출되지 않도록 로딩 상태를 표시한다. */
@Composable
private fun SessionRestoringIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

private fun SessionState.startDestination(): String {
    return when (this) {
        SessionState.Authenticated -> HopesDestination.Home.route
        SessionState.Unauthenticated -> AUTH_ROUTE
        SessionState.Loading -> AUTH_ROUTE
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
