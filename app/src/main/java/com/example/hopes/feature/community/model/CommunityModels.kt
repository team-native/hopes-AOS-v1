package com.example.hopes.feature.community.model

import java.time.LocalDateTime

/**
 * 커뮤니티 게시글이다.
 *
 * @param cohort 게시판 기수다. null이면 "전체" 게시판 글이다.
 * @param authorCohort 작성자 기수다. 이름·학번은 공개하지 않고 기수만 보여준다.
 * @param isMine 내가 쓴 글이면 true다. 더보기에서 수정·삭제를, 아니면 신고를 보여준다.
 * @param isEdited 수정한 글이면 true다. 시간 옆에 "수정됨"을 붙인다.
 */
data class CommunityPost(
    val id: Long,
    val cohort: Int?,
    val authorCohort: Int,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val isMine: Boolean = false,
    val isEdited: Boolean = false,
)

/** 게시글에 달린 댓글이다. */
data class CommunityComment(
    val id: Long,
    val postId: Long,
    val authorCohort: Int,
    val content: String,
    val createdAt: LocalDateTime,
    val isMine: Boolean = false,
    val isEdited: Boolean = false,
)

/** 내 활동 "댓글" 탭의 한 줄이다. 댓글과 그 댓글이 달린 글의 게시판·제목을 함께 담는다. */
data class CommunityMyComment(
    val comment: CommunityComment,
    val postCohort: Int?,
    val postTitle: String,
)

/** 신고 사유다(피그마 17). */
enum class CommunityReportReason {
    Abuse,
    Spam,
    PersonalInfo,
    Inappropriate,
    Other,
}

/** 피그마 시안의 기수 게시판 목록이다. 서버에서 기수 목록을 받게 되면 대체한다. */
val COMMUNITY_COHORTS: List<Int> = listOf(8, 9, 10, 11)
