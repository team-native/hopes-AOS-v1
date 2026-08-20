package com.example.hopes.navigation

import java.net.URLDecoder
import java.net.URLEncoder

/** UI 데모에서 이동 가능한 최상위 화면 경로를 정의한다. */
enum class HopesDestination(
    val route: String,
) {
    Home("home"),
    Chat("chat"),
    History("history"),
    Settings("settings"),
    AppSettings("app_settings"),
    MyPage("my_page"),
    PersonalSettings("personal_settings"),
    Contact("contact"),
    ChatDetail("chat_detail/{$CHAT_DETAIL_ARGUMENT}?$CHAT_DETAIL_QUESTION_ARGUMENT={$CHAT_DETAIL_QUESTION_ARGUMENT}"),
}

const val CHAT_DETAIL_ARGUMENT = "chatId"
const val CHAT_DETAIL_QUESTION_ARGUMENT = "question"
const val CHAT_NEW_CHAT_ARGUMENT = "newChat"

/** 아직 서버에 생성되지 않은 대화를 상세 화면에서 바로 만들 때 쓰는 chatId 자리값이다. */
const val NEW_CHAT_ID = -1L

fun chatRoutePattern(): String = "${HopesDestination.Chat.route}?$CHAT_NEW_CHAT_ARGUMENT={$CHAT_NEW_CHAT_ARGUMENT}"

fun chatRoute(isNewChatRequested: Boolean): String =
    "${HopesDestination.Chat.route}?$CHAT_NEW_CHAT_ARGUMENT=$isNewChatRequested"

/** 대화 식별자를 경로에 포함해 상세 화면이 선택된 대화를 정확히 복원하게 한다. */
fun chatDetailRoute(chatId: Long): String = "chat_detail/$chatId"

/** 첫 질문을 곧바로 상세 화면으로 들고 가서, 대화 생성을 상세 화면 자체에서 진행하게 한다. */
fun chatDetailCreateRoute(question: String): String {
    val encodedQuestion = URLEncoder.encode(question, "UTF-8")
    return "chat_detail/$NEW_CHAT_ID?$CHAT_DETAIL_QUESTION_ARGUMENT=$encodedQuestion"
}

fun decodeChatDetailQuestion(encodedQuestion: String?): String {
    if (encodedQuestion.isNullOrEmpty()) return ""
    return URLDecoder.decode(encodedQuestion, "UTF-8")
}
