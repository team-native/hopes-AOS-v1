package com.example.hopes.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hopes.feature.auth.presentation.AuthRoute
import com.example.hopes.feature.chat.presentation.ChatRoute
import com.example.hopes.feature.detail.presentation.DetailRoute
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailScreenType
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.feature.history.presentation.HistoryRoute
import com.example.hopes.feature.home.presentation.HomeRoute
import com.example.hopes.feature.settings.presentation.SettingsRoute

private const val AUTH_ROUTE = "auth"

/**
 * 인증·탭·상세 화면의 단일 탐색 그래프다.
 * 서버 없이 대화 및 폼 데이터를 navigation scope에 보관해 탭 왕복과 회전에도 유지한다.
 */
@Composable
fun HopesNavigation(
    isDarkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    val hopesNavController = rememberNavController()
    var conversations by rememberSaveable(stateSaver = DemoConversationsSaver) {
        mutableStateOf(emptyList())
    }
    var nextConversationIndex by rememberSaveable { mutableIntStateOf(1) }
    var profileName by rememberSaveable { mutableStateOf("") }
    var profileIntroduction by rememberSaveable { mutableStateOf("") }
    var isProfileSaved by rememberSaveable { mutableStateOf(false) }
    var personalPrompt by rememberSaveable { mutableStateOf("") }
    var isPromptSaved by rememberSaveable { mutableStateOf(false) }
    var contactEmail by rememberSaveable { mutableStateOf("") }
    var contactMessage by rememberSaveable { mutableStateOf("") }
    var isContactSent by rememberSaveable { mutableStateOf(false) }

    fun createOrFindConversation(question: String): DemoConversation {
        return conversations.firstOrNull { conversation ->
            conversation.question == question
        } ?: DemoConversation(
            id = "conversation-$nextConversationIndex",
            question = question,
        ).also { conversation ->
            nextConversationIndex += 1
            conversations = conversations + conversation
        }
    }

    fun navigateToConversation(question: String) {
        val conversation = createOrFindConversation(question)
        hopesNavController.navigate(chatDetailRoute(conversation.id)) {
            launchSingleTop = true
        }
    }

    fun updateConversation(
        conversationId: String,
        transform: (DemoConversation) -> DemoConversation,
    ) {
        conversations = conversations.map { conversation ->
            if (conversation.id == conversationId) {
                transform(conversation)
            } else {
                conversation
            }
        }
    }

    fun detailUiState(conversationId: String? = null): DetailUiState {
        return DetailUiState(
            conversation = conversations.firstOrNull { conversation ->
                conversation.id == conversationId
            },
            profileName = profileName,
            profileIntroduction = profileIntroduction,
            isProfileSaved = isProfileSaved,
            personalPrompt = personalPrompt,
            isPromptSaved = isPromptSaved,
            contactEmail = contactEmail,
            contactMessage = contactMessage,
            isContactSent = isContactSent,
        )
    }

    fun handleDetailEvent(
        detailEvent: DetailScreenEvent,
        conversationId: String? = null,
    ) {
        when (detailEvent) {
            DetailScreenEvent.BackClicked -> hopesNavController.popBackStack()
            DetailScreenEvent.AppSettingsClicked -> {
                hopesNavController.navigate(HopesDestination.AppSettings.route) {
                    launchSingleTop = true
                }
            }

            DetailScreenEvent.ConversationSaveClicked -> {
                conversationId?.let { id ->
                    updateConversation(id) { conversation ->
                        conversation.copy(isSaved = !conversation.isSaved)
                    }
                }
            }

            is DetailScreenEvent.ReplyChanged -> {
                conversationId?.let { id ->
                    updateConversation(id) { conversation ->
                        conversation.copy(replyDraft = detailEvent.value)
                    }
                }
            }
            DetailScreenEvent.ReplySubmitted -> {
                conversationId?.let { id ->
                    updateConversation(id) { conversation ->
                        val submittedReply = conversation.replyDraft.trim()
                        if (submittedReply.isEmpty()) {
                            conversation
                        } else {
                            conversation.copy(
                                replyDraft = "",
                                replies = conversation.replies + submittedReply,
                            )
                        }
                    }
                }
            }

            is DetailScreenEvent.ProfileNameChanged -> {
                profileName = detailEvent.value
                isProfileSaved = false
            }

            is DetailScreenEvent.ProfileIntroductionChanged -> {
                profileIntroduction = detailEvent.value
                isProfileSaved = false
            }

            DetailScreenEvent.ProfileSaveClicked -> isProfileSaved = true
            is DetailScreenEvent.PersonalPromptChanged -> {
                personalPrompt = detailEvent.value
                isPromptSaved = false
            }

            DetailScreenEvent.PersonalPromptSaveClicked -> isPromptSaved = true
            is DetailScreenEvent.ContactEmailChanged -> {
                contactEmail = detailEvent.value
                isContactSent = false
            }

            is DetailScreenEvent.ContactMessageChanged -> {
                contactMessage = detailEvent.value
                isContactSent = false
            }

            DetailScreenEvent.ContactSendClicked -> {
                if (contactEmail.isNotBlank() && contactMessage.isNotBlank()) {
                    isContactSent = true
                }
            }
        }
    }

    NavHost(
        navController = hopesNavController,
        startDestination = AUTH_ROUTE,
        // 모든 화면 전환을 즉시 반영해 Navigation 기본 애니메이션을 사용하지 않는다.
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(AUTH_ROUTE) {
            AuthRoute(
                onAuthenticated = {
                    hopesNavController.navigate(HopesDestination.Home.route) {
                        popUpTo(AUTH_ROUTE) {
                            inclusive = true
                        }
                    }
                },
            )
        }
        composable(HopesDestination.Home.route) {
            HomeRoute(onNavigate = hopesNavController::navigateToTopLevel)
        }
        composable(HopesDestination.Chat.route) {
            ChatRoute(
                onNavigate = hopesNavController::navigateToTopLevel,
                onNavigateToChatDetail = ::navigateToConversation,
            )
        }
        composable(HopesDestination.History.route) {
            HistoryRoute(
                conversations = conversations,
                onNavigate = hopesNavController::navigateToTopLevel,
                onNavigateToChatDetail = ::navigateToConversation,
            )
        }
        composable(HopesDestination.Settings.route) {
            DetailRoute(
                screenType = DetailScreenType.MyPage,
                uiState = detailUiState(),
                onEvent = { detailEvent ->
                    handleDetailEvent(detailEvent)
                },
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
        composable(HopesDestination.AppSettings.route) {
            SettingsRoute(
                onNavigate = hopesNavController::navigateToTopLevel,
                isDarkModeEnabled = isDarkThemeEnabled,
                onDarkModeChange = onDarkThemeChange,
                onBackClick = hopesNavController::popBackStack,
                onNavigateToPersonalSettings = {
                    hopesNavController.navigate(HopesDestination.PersonalSettings.route) {
                        launchSingleTop = true
                    }
                },
                onNavigateToContact = {
                    hopesNavController.navigate(HopesDestination.Contact.route) {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    hopesNavController.navigate(AUTH_ROUTE) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(HopesDestination.PersonalSettings.route) {
            DetailRoute(
                screenType = DetailScreenType.PersonalSettings,
                uiState = detailUiState(),
                onEvent = { detailEvent ->
                    handleDetailEvent(detailEvent)
                },
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
        composable(HopesDestination.Contact.route) {
            DetailRoute(
                screenType = DetailScreenType.Contact,
                uiState = detailUiState(),
                onEvent = { detailEvent ->
                    handleDetailEvent(detailEvent)
                },
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
        composable(
            route = HopesDestination.ChatDetail.route,
            arguments = listOf(
                navArgument(CHAT_DETAIL_ARGUMENT) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString(CHAT_DETAIL_ARGUMENT)
            DetailRoute(
                screenType = DetailScreenType.ChatDetail,
                uiState = detailUiState(conversationId),
                onEvent = { detailEvent ->
                    handleDetailEvent(detailEvent, conversationId)
                },
                onNavigate = hopesNavController::navigateToTopLevel,
            )
        }
    }
}

/** 탭 전환은 중복 백스택을 만들지 않고 각 탭의 상태를 보존한다. */
private fun NavHostController.navigateToTopLevel(destination: HopesDestination) {
    navigate(destination.route) {
        launchSingleTop = true
        popUpTo(HopesDestination.Home.route) {
            saveState = true
        }
        restoreState = true
    }
}

/** 대화 모델을 Android 저장 가능 값만으로 직렬화한다. */
private val DemoConversationsSaver = Saver<List<DemoConversation>, List<Any>>(
    save = { conversations ->
        buildList {
            conversations.forEach { conversation ->
                add(conversation.id)
                add(conversation.question)
                add(conversation.isSaved)
                add(conversation.replyDraft)
                // Bundle에 저장 가능한 String ArrayList로 답글 이력을 보관한다.
                add(ArrayList(conversation.replies))
            }
        }
    },
    restore = { savedValues ->
        savedValues.chunked(5).mapNotNull { savedConversation ->
            val id = savedConversation.getOrNull(0) as? String
            val question = savedConversation.getOrNull(1) as? String
            val isSaved = savedConversation.getOrNull(2) as? Boolean
            val replyDraft = savedConversation.getOrNull(3) as? String
            val replies = (savedConversation.getOrNull(4) as? ArrayList<*>)
                ?.filterIsInstance<String>()
                .orEmpty()

            if (id != null && question != null && isSaved != null && replyDraft != null) {
                DemoConversation(
                    id = id,
                    question = question,
                    isSaved = isSaved,
                    replyDraft = replyDraft,
                    replies = replies,
                )
            } else {
                null
            }
        }
    },
)
