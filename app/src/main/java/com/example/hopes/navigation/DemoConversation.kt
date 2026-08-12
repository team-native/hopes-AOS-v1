package com.example.hopes.navigation

/** 채팅과 기록 화면이 공유하는 로컬 데모 대화다. */
data class DemoConversation(
    val id: String,
    val question: String,
    val isSaved: Boolean = false,
    // 대화별 초안과 답글 이력을 분리해 탭·상세 이동 후에도 서로 섞이지 않게 한다.
    val replyDraft: String = "",
    val replies: List<String> = emptyList(),
)
