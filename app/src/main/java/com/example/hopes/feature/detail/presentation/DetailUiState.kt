package com.example.hopes.feature.detail.presentation

/** 탐색 그래프가 소유하는 개인 설정·문의 화면의 로컬 입력 상태다. */
data class DetailUiState(
    val personalPrompt: String = "",
    val isPromptSaved: Boolean = false,
    val contactEmail: String = "",
    val contactMessage: String = "",
    val isContactSent: Boolean = false,
)
