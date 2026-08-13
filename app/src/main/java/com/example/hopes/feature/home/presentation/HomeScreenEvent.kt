package com.example.hopes.feature.home.presentation

sealed interface HomeScreenEvent {
    data object StartChatClicked : HomeScreenEvent
}
