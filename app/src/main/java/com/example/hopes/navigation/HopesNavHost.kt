package com.example.hopes.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.hopes.core.session.SessionState
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.auth.presentation.SessionViewModel
import com.example.hopes.feature.chat.presentation.ChatRoute
import com.example.hopes.feature.chat.presentation.detail.ChatDetailRoute
import com.example.hopes.feature.detail.presentation.ContactRoute
import com.example.hopes.feature.detail.presentation.MyPageRoute
import com.example.hopes.feature.detail.presentation.PersonalSettingsRoute
import com.example.hopes.feature.history.presentation.HistoryRoute
import com.example.hopes.feature.home.presentation.HomeRoute
import com.example.hopes.feature.settings.presentation.SettingsRoute

/** 인증·탭·설정 및 서버 채팅 상세의 단일 탐색 그래프다. */
@Composable
fun HopesNavHost(
    isDarkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()

    if (sessionState is SessionState.Loading) {
        SessionRestoringIndicator()
        return
    }

    // SessionState가 로그인/로그아웃뿐 아니라 임의 화면의 401 응답으로도 조용히 바뀔 수 있어(RepositoryImpls의
    // expireSessionOnUnauthorized), 로컬 boolean 토글이 아니라 sessionState 자체를 key로 삼아 전체를 다시 구성한다.
    key(sessionState) {
        if (sessionState is SessionState.Authenticated) {
            HopesMainNavDisplay(
                isDarkThemeEnabled = isDarkThemeEnabled,
                onDarkThemeChange = onDarkThemeChange,
            )
        } else {
            HopesAuthNavDisplay()
        }
    }
}

@Composable
private fun HopesAuthNavDisplay() {
    val backStack = rememberNavBackStack(HopesDestination.Auth)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        predictivePopTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        entryProvider = entryProvider {
            entry<HopesDestination.Auth> {
                // 인증 성공은 sessionState 변화 + key(sessionState)가 이미 처리하므로 별도 백스택 조작이 필요 없다.
                AuthRoute(onAuthenticated = {})
            }
        },
    )
}

@Composable
private fun HopesMainNavDisplay(
    isDarkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    val navigationState = rememberHopesMainNavigationState(
        startRoute = HopesDestination.Home,
        topLevelRoutes = hopesTabDestinations,
    )
    val navigator = remember(navigationState) { HopesMainNavigator(navigationState) }

    NavDisplay(
        entries = navigationState.toEntries(
            entryProvider {
                entry<HopesDestination.Home> {
                    HomeRoute(onNavigate = navigator::navigateToTab)
                }
                entry<HopesDestination.Chat> { key ->
                    ChatRoute(
                        onNavigate = navigator::navigateToTab,
                        onNavigateToNewChatDetail = { question ->
                            navigator.push(HopesDestination.ChatDetail(chatId = NEW_CHAT_ID, question = question))
                        },
                        isNewChatRequested = key.isNewChatRequested,
                    )
                }
                entry<HopesDestination.History> {
                    HistoryRoute(
                        onNavigate = navigator::navigateToTab,
                        onNavigateToChatDetail = { chatId ->
                            navigator.push(HopesDestination.ChatDetail(chatId = chatId))
                        },
                        onStartNewChat = navigator::navigateToNewChat,
                    )
                }
                entry<HopesDestination.Settings> {
                    MyPageRoute(
                        onNavigate = navigator::navigateToTab,
                        // 설정은 마이페이지 하위 화면이라 top-level 탐색이 아닌 일반 push로 진입시켜,
                        // 뒤로가기 시 홈이 아닌 마이페이지로 돌아오게 한다.
                        onNavigateToAppSettings = { navigator.push(HopesDestination.AppSettings) },
                    )
                }
                entry<HopesDestination.AppSettings> {
                    SettingsRoute(
                        onNavigate = navigator::navigateToTab,
                        isDarkModeEnabled = isDarkThemeEnabled,
                        onDarkModeChange = onDarkThemeChange,
                        onBackClick = navigator::goBack,
                        onNavigateToPersonalSettings = { navigator.push(HopesDestination.PersonalSettings) },
                        onNavigateToContact = { navigator.push(HopesDestination.Contact) },
                        // 로그아웃도 sessionState 변화 + key(sessionState)가 이미 처리하므로 별도 백스택 조작이 필요 없다.
                        onLogout = {},
                    )
                }
                entry<HopesDestination.PersonalSettings> {
                    PersonalSettingsRoute(
                        onNavigate = navigator::navigateToTab,
                        onBackClick = navigator::goBack,
                    )
                }
                entry<HopesDestination.Contact> {
                    ContactRoute(
                        onNavigate = navigator::navigateToTab,
                        onBackClick = navigator::goBack,
                    )
                }
                entry<HopesDestination.ChatDetail> { key ->
                    ChatDetailRoute(
                        chatId = key.chatId,
                        question = key.question,
                        onBackClick = navigator::goBack,
                        onNavigate = navigator::navigateToTab,
                    )
                }
            },
        ),
        onBack = navigator::goBack,
        transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        predictivePopTransitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
    )
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
