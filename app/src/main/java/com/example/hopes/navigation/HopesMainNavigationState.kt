package com.example.hopes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * 하단 탭(Home/Chat/History/Settings)마다 독립된 back stack을 유지한다.
 * 탭을 전환해도 이전에 그 탭에서 쌓아 둔 화면들이 그대로 보존된다.
 */
internal class HopesMainNavigationState(
    val startRoute: HopesDestination,
    private val topLevelRoutes: List<HopesDestination>,
    topLevelIndex: MutableState<Int>,
    val backStacks: Map<HopesDestination, NavBackStack<NavKey>>,
) {
    private var topLevelIndex: Int by topLevelIndex

    /** [HopesDestination]은 kotlinx.serialization 대상이라 rememberSaveable 기본 Saver로 못 담기 때문에 index로 저장한다. */
    var topLevelRoute: HopesDestination
        get() = topLevelRoutes[topLevelIndex]
        set(value) {
            val index = topLevelRoutes.indexOf(value)
            if (index >= 0) topLevelIndex = index
        }

    val currentBackStack: NavBackStack<NavKey>
        get() = backStacks.getValue(topLevelRoute)
}

@Composable
internal fun rememberHopesMainNavigationState(
    startRoute: HopesDestination,
    topLevelRoutes: List<HopesDestination>,
): HopesMainNavigationState {
    val topLevelIndex = rememberSaveable { mutableStateOf(topLevelRoutes.indexOf(startRoute).coerceAtLeast(0)) }
    val backStacks = topLevelRoutes.associateWith { route -> rememberNavBackStack(route) }

    return remember(startRoute, topLevelRoutes) {
        HopesMainNavigationState(
            startRoute = startRoute,
            topLevelRoutes = topLevelRoutes,
            topLevelIndex = topLevelIndex,
            backStacks = backStacks,
        )
    }
}

/** 현재 활성 탭의 back stack을 [NavEntry] 목록으로 변환한다. 엔트리별 ViewModel/상태 저장 범위를 함께 부여한다. */
@Composable
internal fun HopesMainNavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val viewModelStoreDecorator = rememberViewModelStoreNavEntryDecorator<NavKey>()
    val savedStateDecorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(savedStateDecorator, viewModelStoreDecorator),
            entryProvider = entryProvider,
        )
    }

    return decoratedEntries.getValue(topLevelRoute).toMutableStateList()
}

/** 탭 전환/화면 push/pop을 담당한다. NavController를 대체한다. */
internal class HopesMainNavigator(private val state: HopesMainNavigationState) {

    /** 하단 탭 선택 시 호출한다. */
    fun navigateToTab(destination: HopesDestination) {
        if (destination in state.backStacks.keys) {
            state.topLevelRoute = destination
        }
    }

    /** 현재 탭의 back stack에 화면을 push한다. */
    fun push(destination: HopesDestination) {
        state.currentBackStack.add(destination)
    }

    /** 시스템/UI 뒤로가기: 현재 탭 back stack에서 pop하고, 탭의 시작 화면까지 pop되면 시작 탭으로 되돌린다. */
    fun goBack() {
        val stack = state.currentBackStack
        if (stack.size > 1) {
            stack.removeLastOrNull()
        } else if (state.topLevelRoute != state.startRoute) {
            state.topLevelRoute = state.startRoute
        }
    }

    /** 기록 화면의 "새 대화 시작": Chat 탭 back stack을 비우고 새 Chat(isNewChatRequested=true)만 남긴 뒤 그 탭으로 전환한다. */
    fun navigateToNewChat() {
        val chatBackStack = state.backStacks.getValue(HopesDestination.Chat())
        while (chatBackStack.isNotEmpty()) {
            chatBackStack.removeLastOrNull()
        }
        chatBackStack.add(HopesDestination.Chat(isNewChatRequested = true))
        state.topLevelRoute = HopesDestination.Chat()
    }
}
