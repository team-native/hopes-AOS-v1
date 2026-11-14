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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.session.SessionState
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.auth.presentation.SessionViewModel
import com.example.hopes.feature.chat.detail.view.ChatDetailRoute
import com.example.hopes.feature.chat.view.ChatRoute
import com.example.hopes.feature.community.presentation.CommunityRoute
import com.example.hopes.feature.detail.presentation.ContactRoute
import com.example.hopes.feature.detail.presentation.MyPageRoute
import com.example.hopes.feature.detail.presentation.PersonalSettingsRoute
import com.example.hopes.feature.detail.presentation.ProfileEditRoute
import com.example.hopes.feature.history.presentation.HistoryRoute
import com.example.hopes.feature.home.presentation.HomeRoute
import com.example.hopes.feature.notification.presentation.NotificationRoute
import com.example.hopes.feature.settings.presentation.AppInfoScreen
import com.example.hopes.feature.settings.presentation.SettingsRoute
import com.teamnative.hopes.BuildConfig
import com.teamnative.hopes.R

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

    // 401 응답으로 세션이 끊기면(Expired) 보던 화면을 그대로 둔 채 로그인 만료 안내를 띄우므로,
    // Authenticated와 Expired는 같은 메인 화면으로 묶어 key가 바뀌지 않게 한다.
    // 로그인/로그아웃처럼 메인↔인증이 바뀔 때만 전체를 다시 구성한다.
    val isMainVisible = sessionState is SessionState.Authenticated || sessionState is SessionState.Expired

    key(isMainVisible) {
        if (isMainVisible) {
            HopesMainNavDisplay(
                isDarkThemeEnabled = isDarkThemeEnabled,
                onDarkThemeChange = onDarkThemeChange,
            )
        } else {
            HopesAuthNavDisplay()
        }
    }

    if (sessionState is SessionState.Expired) {
        HopesAlertDialog(
            title = stringResource(R.string.session_expired_title),
            message = stringResource(R.string.session_expired_message),
            dismissText = stringResource(R.string.close),
            confirmText = stringResource(R.string.session_expired_login),
            // 세션이 이미 끊겨 메인 화면을 계속 쓸 수 없으므로 닫기와 로그인 모두 인증 화면으로 보낸다.
            onDismiss = sessionViewModel::acknowledgeExpiredSession,
            onConfirm = sessionViewModel::acknowledgeExpiredSession,
            isDismissibleByOutside = false,
        )
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
        entryProvider =
            entryProvider {
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
    val navigationState =
        rememberHopesMainNavigationState(
            startRoute = HopesDestination.Home,
            topLevelRoutes = hopesTabDestinations,
        )
    val navigator = remember(navigationState) { HopesMainNavigator(navigationState) }
    // 프로필 수정 화면에서 저장하고 돌아온 직후 마이페이지에 "프로필을 저장했어요" 토스트를 띄우기 위한 신호다.
    var isProfileSavedToastVisible by rememberSaveable { mutableStateOf(false) }

    NavDisplay(
        entries =
            navigationState.toEntries(
                entryProvider {
                    entry<HopesDestination.Home> {
                        HomeRoute(
                            onNavigate = navigator::navigateToTab,
                            onNotificationClick = { navigator.push(HopesDestination.Notifications) },
                        )
                    }
                    entry<HopesDestination.Chat> { key ->
                        ChatRoute(
                            onNavigate = navigator::navigateToTab,
                            onNavigateToNewChatDetail = { question, department, major ->
                                navigator.push(
                                    HopesDestination.ChatDetail(
                                        chatId = NEW_CHAT_ID,
                                        question = question,
                                        department = department,
                                        major = major,
                                    ),
                                )
                            },
                            isNewChatRequested = key.isNewChatRequested,
                        )
                    }
                    entry<HopesDestination.Community> {
                        CommunityRoute(onNavigate = navigator::navigateToTab)
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
                            onNavigateToProfileEdit = { navigator.push(HopesDestination.ProfileEdit) },
                            onNavigateToPersonalSettings = { navigator.push(HopesDestination.PersonalSettings) },
                            onNavigateToNotifications = { navigator.push(HopesDestination.Notifications) },
                            onNavigateToContact = { navigator.push(HopesDestination.Contact) },
                            isProfileSavedToastVisible = isProfileSavedToastVisible,
                            onProfileSavedToastDismiss = { isProfileSavedToastVisible = false },
                        )
                    }
                    entry<HopesDestination.AppSettings> {
                        SettingsRoute(
                            isDarkModeEnabled = isDarkThemeEnabled,
                            onDarkModeChange = onDarkThemeChange,
                            onBackClick = navigator::goBack,
                            onNavigateToAppInfo = { navigator.push(HopesDestination.AppInfo) },
                            // 로그아웃도 sessionState 변화 + key(sessionState)가 이미 처리하므로 별도 백스택 조작이 필요 없다.
                            onLogout = {},
                        )
                    }
                    entry<HopesDestination.PersonalSettings> {
                        PersonalSettingsRoute(onBackClick = navigator::goBack)
                    }
                    entry<HopesDestination.Contact> {
                        ContactRoute(onBackClick = navigator::goBack)
                    }
                    entry<HopesDestination.ProfileEdit> {
                        ProfileEditRoute(
                            onBackClick = navigator::goBack,
                            onProfileSaved = {
                                isProfileSavedToastVisible = true
                                navigator.goBack()
                            },
                        )
                    }
                    entry<HopesDestination.AppInfo> {
                        AppInfoScreen(
                            appVersion = BuildConfig.VERSION_NAME,
                            onBackClick = navigator::goBack,
                        )
                    }
                    entry<HopesDestination.Notifications> {
                        NotificationRoute(onBackClick = navigator::goBack)
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
