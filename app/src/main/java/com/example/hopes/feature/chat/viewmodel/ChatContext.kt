package com.example.hopes.feature.chat.viewmodel

/** 채팅 홈에서 고르는 학과다. 화면 표시 문구는 view 계층이 결정한다. */
enum class ChatDepartment {
    Software,
    SmartIot,
    ArtificialIntelligence,
}

/** 채팅 홈에서 고르는 관심 전공이다. [Other]를 뺀 나머지는 전공 이름을 그대로 화면에 쓴다. */
enum class ChatMajor(
    val displayName: String?,
) {
    Ios("iOS"),
    Android("Android"),
    Frontend("Frontend"),
    Backend("Backend"),
    Ai("AI"),
    Iot("IoT"),
    DevOps("DevOps"),
    Design("Design"),
    Other(null),
}

/** 추천 질문의 주제다. 주제마다 아이콘과 질문 문구가 정해진다. */
enum class ChatQuestionTopic {
    Dormitory,
    Admission,
    MajorStudy,
    Advice,
}

/** 추천 질문 영역의 상태다. 추천 API 연동 전이라 지금은 항상 [Loaded]다. */
enum class ChatRecommendationState {
    Loaded,
    Loading,
    LoadError,
}

/**
 * 학과·전공 선택에 맞춘 추천 질문 주제 목록이다.
 * 둘 다 골랐으면 전공 공부 질문을 세 번째에 끼워 넣고, 하나라도 비었으면 기본 질문 3개만 돌려준다.
 * 전공이 "기타"면 전공 이름을 질문에 넣을 수 없어 기본 질문만 쓴다.
 */
fun recommendedQuestionTopics(
    department: ChatDepartment?,
    major: ChatMajor?,
): List<ChatQuestionTopic> {
    val defaultTopics = listOf(ChatQuestionTopic.Dormitory, ChatQuestionTopic.Admission, ChatQuestionTopic.Advice)
    if (department == null || major == null || major.displayName == null) return defaultTopics

    return listOf(
        ChatQuestionTopic.Dormitory,
        ChatQuestionTopic.Admission,
        ChatQuestionTopic.MajorStudy,
        ChatQuestionTopic.Advice,
    )
}
