package com.example.hopes.feature.detail.presentation

import com.example.hopes.navigation.DemoConversation

/** 탐색 그래프가 소유하는 상세 화면의 로컬 데모 상태다. */
data class DetailUiState(
    val conversation: DemoConversation? = null,
    val profileName: String = "",
    val profileIntroduction: String = "",
    val isProfileSaved: Boolean = false,
    val personalPrompt: String = "",
    val isPromptSaved: Boolean = false,
    val contactEmail: String = "",
    val contactMessage: String = "",
    val isContactSent: Boolean = false,
)
