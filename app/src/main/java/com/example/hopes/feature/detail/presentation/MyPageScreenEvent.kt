package com.example.hopes.feature.detail.presentation

/** 마이페이지에서 하위 화면으로 이동하려는 사용자 의도다. 모두 MyPageRoute가 화면 이동으로 처리한다. */
sealed interface MyPageScreenEvent {
    data object AppSettingsClicked : MyPageScreenEvent

    data object ProfileEditClicked : MyPageScreenEvent

    data object MyActivityClicked : MyPageScreenEvent

    data object PersonalSettingsClicked : MyPageScreenEvent

    data object NotificationClicked : MyPageScreenEvent

    data object ContactClicked : MyPageScreenEvent
}
