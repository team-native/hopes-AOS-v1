package com.example.hopes.feature.detail.presentation

/** 상세 화면에서 발생하는 로컬 입력·저장·탐색 의도다. */
sealed interface DetailScreenEvent {
    data object BackClicked : DetailScreenEvent

    data object AppSettingsClicked : DetailScreenEvent

    data object ConversationSaveClicked : DetailScreenEvent

    data class ReplyChanged(val value: String) : DetailScreenEvent

    data object ReplySubmitted : DetailScreenEvent

    data class ProfileNameChanged(val value: String) : DetailScreenEvent

    data class ProfileIntroductionChanged(val value: String) : DetailScreenEvent

    data object ProfileSaveClicked : DetailScreenEvent

    data class PersonalPromptChanged(val value: String) : DetailScreenEvent

    data object PersonalPromptSaveClicked : DetailScreenEvent

    data class ContactEmailChanged(val value: String) : DetailScreenEvent

    data class ContactMessageChanged(val value: String) : DetailScreenEvent

    data object ContactSendClicked : DetailScreenEvent
}
