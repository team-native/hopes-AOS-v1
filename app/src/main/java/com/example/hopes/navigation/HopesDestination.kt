package com.example.hopes.navigation

import androidx.navigation3.runtime.NavKey
import com.example.hopes.feature.chat.detail.viewmodel.ChatAttachmentArg
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

    /** 커뮤니티 게시글 상세다(피그마 14). */
    @Serializable data class CommunityPost(
        val postId: Long,
    ) : HopesDestination

    /**
     * 글쓰기(피그마 15)다. [postId]가 있으면 그 글을 수정한다.
     *
     * @param defaultCohort 새 글의 기본 게시판이다.
     * @param isOpenedFromList 커뮤니티 목록에서 열었으면 true다. 등록 후 목록에 "게시글을 등록했어요"를 띄운다.
     */
    @Serializable data class CommunityWrite(
        val postId: Long? = null,
        val defaultCohort: Int? = null,
        val isOpenedFromList: Boolean = false,
    ) : HopesDestination

    /** 게시글 검색(피그마 16)이다. [cohort] 게시판 안에서 찾는다. */
    @Serializable data class CommunitySearch(
        val cohort: Int? = null,
    ) : HopesDestination

    /** 내 활동(피그마 18)이다. */
    @Serializable data object CommunityMyActivity : HopesDestination

    // Settings 라우트는 실제로 MyPageRoute(마이페이지)를 그린다. 기존 네이밍 불일치는 이번 마이그레이션에서 다루지 않는다.
    @Serializable data object Settings : HopesDestination

    @Serializable data object AppSettings : HopesDestination

    @Serializable data object AccountDeletion : HopesDestination

    @Serializable data object PersonalSettings : HopesDestination

    @Serializable data object Contact : HopesDestination

    @Serializable data object ProfileEdit : HopesDestination

    @Serializable data object AppInfo : HopesDestination

    /** 채팅 위치 안내에서 "지도 보기"로 여는 학교 지도다(피그마 22). */
    @Serializable data class SchoolMap(
        val buildingName: String,
        val accuracyMeters: Int? = null,
    ) : HopesDestination

    /**
     * @param department 채팅 홈에서 고른 학과다. 상세 화면 부제목("소프트웨어개발과 · Frontend")에 쓴다.
     */
    @Serializable data class ChatDetail(
        val chatId: Long,
        val question: String = "",
        val attachments: List<ChatAttachmentArg> = emptyList(),
        val department: ChatDepartment? = null,
        val major: ChatMajor? = null,
        val departmentId: Long? = null,
        val specialtyId: Long? = null,
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
