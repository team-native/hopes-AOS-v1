package com.example.hopes.navigation

import androidx.navigation3.runtime.NavKey
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import kotlinx.serialization.Serializable

/** 앱의 모든 화면을 나타내는 Navigation 3 back stack 키다. */
@Serializable
sealed interface HopesDestination : NavKey {
    @Serializable data object Home : HopesDestination

    @Serializable data class Chat(
        val isNewChatRequested: Boolean = false,
    ) : HopesDestination

    @Serializable data object Community : HopesDestination

    @Serializable data object History : HopesDestination

    // Settings 라우트는 실제로 MyPageRoute(마이페이지)를 그린다. 기존 네이밍 불일치는 이번 마이그레이션에서 다루지 않는다.
    @Serializable data object Settings : HopesDestination

    @Serializable data object AppSettings : HopesDestination

    @Serializable data object PersonalSettings : HopesDestination

    @Serializable data object Contact : HopesDestination

    @Serializable data object ProfileEdit : HopesDestination

    @Serializable data object Notifications : HopesDestination

    @Serializable data object AppInfo : HopesDestination

    /**
     * @param department 채팅 홈에서 고른 학과다. 상세 화면 부제목("소프트웨어개발과 · Frontend")에 쓴다.
     */
    @Serializable data class ChatDetail(
        val chatId: Long,
        val question: String = "",
        val department: ChatDepartment? = null,
        val major: ChatMajor? = null,
    ) : HopesDestination

    @Serializable data object Auth : HopesDestination
}

/** 하단 탭에 표시되는 최상위 목적지 5개를 기본 순서대로 제공한다. */
val hopesTabDestinations: List<HopesDestination> =
    listOf(
        HopesDestination.Home,
        HopesDestination.Chat(),
        HopesDestination.Community,
        HopesDestination.History,
        HopesDestination.Settings,
    )

/** 아직 서버에 생성되지 않은 대화를 상세 화면에서 바로 만들 때 쓰는 chatId 자리값이다. */
const val NEW_CHAT_ID = -1L
