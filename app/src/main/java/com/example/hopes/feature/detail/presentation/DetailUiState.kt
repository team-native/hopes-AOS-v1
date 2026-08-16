package com.example.hopes.feature.detail.presentation

/** 탐색 그래프가 소유하는 설정 화면의 로컬 입력 상태다. */
data class DetailUiState(
    val profileName: String = "",
    val profileIntroduction: String = "",
    val isProfileSaved: Boolean = false,
    val personalPrompt: String = "",
    val isPromptSaved: Boolean = false,
    val contactEmail: String = "",
    val contactMessage: String = "",
    val isContactSent: Boolean = false,
)
