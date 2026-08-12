package com.example.hopes.navigation

/** UI 데모에서 이동 가능한 최상위 화면 경로를 정의한다. */
enum class HopesDestination(
    val route: String,
) {
    Home("home"),
    Chat("chat"),
    History("history"),
    Settings("settings"),
}
