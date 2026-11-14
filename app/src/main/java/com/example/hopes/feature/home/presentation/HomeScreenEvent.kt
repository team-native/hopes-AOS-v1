package com.example.hopes.feature.home.presentation

sealed interface HomeScreenEvent {
    data object StartChatClicked : HomeScreenEvent

    data object NotificationClicked : HomeScreenEvent

    data object PopularPostsMoreClicked : HomeScreenEvent

    data object PopularPostsRetryClicked : HomeScreenEvent

    data class PopularPostClicked(
        val postId: Long,
    ) : HomeScreenEvent
}
