package com.example.hopes.feature.chat.detail.viewmodel

/**
 * 서버 대화 상세의 현재 표시 상태다.
 *
 * @param isLoadError 기존 대화를 불러오지 못했다. 목록 대신 불러오기 오류 안내를 보여준다.
 * @param failedQuestion 답변을 받지 못한 질문이다. 그 아래에 실패(08-3) 또는 중지 안내와 다시 시도를 보여준다.
 * @param pendingQuestion 오프라인이라 아직 못 보낸 질문이다. "연결되면 전송돼요"를 붙이고, 연결되면 자동으로 보낸다(08-5).
 * @param followUpQuestions 마지막 답변 아래 "추가로 많이 묻는 질문"이다. API 연동 전이라 지금은 비어 있어 카드가 숨겨진다.
 * @param likedMessageIds 좋아요를 누른 답변 id다. API 연동 전이라 화면에서만 유지한다.
 * @param draftAttachments 입력창 위에 붙여 둔, 아직 보내지 않은 첨부 자료다(피그마 10).
 * @param location 위치 질문에 대한 위치 안내 카드 상태다(피그마 20~23). 위치 기능 연동 전이라 지금은 null이다.
 */
data class ChatDetailUiState(
    val title: String = "",
    val messages: List<ChatMessageUiModel> = emptyList(),
    val replyText: String = "",
    val isLoading: Boolean = true,
    val isLoadError: Boolean = false,
    val isSending: Boolean = false,
    val failedQuestion: String? = null,
    val failureReason: ChatAnswerFailureReason = ChatAnswerFailureReason.Error,
    val pendingQuestion: String? = null,
    val isOffline: Boolean = false,
    val followUpQuestions: List<String> = emptyList(),
    val likedMessageIds: Set<Long> = emptySet(),
    val draftAttachments: List<ChatAttachmentUiModel> = emptyList(),
    val location: ChatLocationUiState? = null,
) {
    /** 답변을 기다리는 중이거나, 대화를 불러오지 못했거나, 오프라인이면 추가 질문을 막는다. */
    val isReplyEnabled: Boolean
        get() = !isLoading && !isLoadError && !isSending && !isOffline && pendingQuestion == null

    /** 업로드 중이거나 보낼 수 없는 첨부(형식·용량·손상)가 있으면 전송을 막는다. 업로드 실패한 첨부는 빼고 보낼 수 있다. */
    val canSendDraftAttachments: Boolean
        get() = draftAttachments.none { it.status is ChatAttachmentStatus.Uploading || it.status.isRejected }
}

/** 답변을 받지 못한 이유다. 안내 문구만 달라지고 다시 시도 동작은 같다. */
enum class ChatAnswerFailureReason {
    Error,
    Stopped,
}

/**
 * @param attachments 내 질문에 붙인 첨부 자료다. 업로드 API 연동 전이라 서버에는 보내지 않고 화면에서만 보여준다.
 */
data class ChatMessageUiModel(
    val id: Long?,
    val content: String,
    val isUser: Boolean,
    val attachments: List<ChatAttachmentUiModel> = emptyList(),
    val sources: List<String> = emptyList(),
)

enum class ChatAttachmentKind {
    Image,
    Pdf,
}

/**
 * 채팅에 첨부한 이미지·PDF 한 개다.
 *
 * @param uri 기기에서 고른 파일의 content URI다. 같은 파일을 두 번 붙이지 않는 기준이기도 하다.
 * @param pageCount PDF 쪽수다. 이미지이거나 쪽수를 읽지 못하면 null이다.
 * @param status 업로드·검사 상태다. 업로드 API 연동 전이라 검사를 통과하면 [ChatAttachmentStatus.Ready]다.
 */
data class ChatAttachmentUiModel(
    val uri: String,
    val kind: ChatAttachmentKind,
    val name: String,
    val pageCount: Int? = null,
    val status: ChatAttachmentStatus = ChatAttachmentStatus.Ready,
)

/** 첨부 한 개의 상태다(피그마 10-2 ~ 10-6). */
sealed interface ChatAttachmentStatus {
    data object Ready : ChatAttachmentStatus

    /** @param progress 0..1 업로드 진행률이다. */
    data class Uploading(
        val progress: Float,
    ) : ChatAttachmentStatus

    /** 업로드에 실패했다. 눌러서 다시 올리거나 빼고 보낼 수 있다(10-3). */
    data object UploadFailed : ChatAttachmentStatus

    /** 이미지·PDF가 아닌 파일이다(10-4). */
    data object Unsupported : ChatAttachmentStatus

    /** 첨부 개수 제한을 넘었다(10-5). 다른 첨부를 빼면 다시 들어갈 수 있다. */
    data object OverLimit : ChatAttachmentStatus

    /** 파일 한 개 용량 제한을 넘었다(10-5). */
    data object TooLarge : ChatAttachmentStatus

    /** 암호가 걸렸거나 손상돼 열 수 없는 PDF다(10-6). */
    data object Unreadable : ChatAttachmentStatus

    /** 보내기 전에 빼야 하는 첨부인지 여부다. */
    val isRejected: Boolean
        get() = this == Unsupported || this == OverLimit || this == TooLarge || this == Unreadable
}

/** 위치 안내 카드 상태다(피그마 20 ~ 23). */
sealed interface ChatLocationUiState {
    /** 위치 권한이 없어 허용을 요청한다(20). */
    data object PermissionRequired : ChatLocationUiState

    /** 현재 위치를 측정하고 있다(20-3). @param progress 0..1 진행률이다. */
    data class Locating(
        val progress: Float,
    ) : ChatLocationUiState

    /** 학교 안 건물 근처에 있다(21). */
    data class NearBuilding(
        val buildingName: String,
        val accuracyMeters: Int,
    ) : ChatLocationUiState

    /** 학교 밖에 있다(21-2). @param distanceKm "2.3"처럼 화면에 그대로 쓸 거리다. */
    data class OutsideSchool(
        val distanceKm: String,
    ) : ChatLocationUiState

    /** 학교 근처지만 정확도가 낮다(21-3). */
    data class LowAccuracy(
        val accuracyMeters: Int,
    ) : ChatLocationUiState

    /** 위치를 확인하지 못했다(23). */
    data object Failed : ChatLocationUiState
}
