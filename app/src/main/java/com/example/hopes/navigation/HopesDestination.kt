package com.example.hopes.navigation

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
    ChatDetail("chat_detail/{$CHAT_DETAIL_ARGUMENT}"),
}

const val CHAT_DETAIL_ARGUMENT = "chatId"

/** 대화 식별자를 경로에 포함해 상세 화면이 선택된 대화를 정확히 복원하게 한다. */
fun chatDetailRoute(chatId: Long): String = "chat_detail/$chatId"
