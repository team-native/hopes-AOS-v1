package com.example.hopes.feature.home.presentation

sealed interface HomeScreenEvent {
    data object StartChatClicked : HomeScreenEvent

    data object PopularPostsMoreClicked : HomeScreenEvent

    data object PopularPostsRetryClicked : HomeScreenEvent

    /** 인기글의 하트를 눌렀다. 켜져 있으면 끄고 꺼져 있으면 켠다. */
    data class PopularPostLikeClicked(
        val postId: Long,
    ) : HomeScreenEvent

    data class PopularPostClicked(
        val postId: Long,
    ) : HomeScreenEvent
}
