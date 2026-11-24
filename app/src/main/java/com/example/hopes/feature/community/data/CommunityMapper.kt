package com.example.hopes.feature.community.data

import com.example.hopes.data.api.CommunityCommentDto
import com.example.hopes.data.api.CommunityMyCommentDto
import com.example.hopes.data.api.CommunityPostDto
import com.example.hopes.data.api.CommunityPostPreviewDto
import com.example.hopes.feature.community.model.CommunityComment
import com.example.hopes.feature.community.model.CommunityMyComment
import com.example.hopes.feature.community.model.CommunityPost
import java.time.LocalDateTime
import java.time.OffsetDateTime

internal fun CommunityPostPreviewDto.toCommunityPost(cohort: Int?): CommunityPost =
    CommunityPost(
        id = id,
        cohort = cohort,
        authorCohort = authorLabel.filter(Char::isDigit).toIntOrNull() ?: 0,
        title = title,
        content = preview,
        createdAt = createdAt.toLocalDateTimeOrNow(),
        likeCount = likeCount,
        commentCount = commentCount,
        isLiked = likedByMe,
        isMine = isMine,
        isEdited = updatedAt != createdAt,
    )

internal fun CommunityPostDto.toCommunityPost(cohort: Int?): CommunityPost =
    CommunityPost(
        id = id,
        cohort = cohort,
        authorCohort = authorLabel.filter(Char::isDigit).toIntOrNull() ?: 0,
        title = title,
        content = content,
        createdAt = createdAt.toLocalDateTimeOrNow(),
        likeCount = likeCount,
        commentCount = commentCount,
        isLiked = likedByMe,
        isMine = isMine,
        isEdited = updatedAt != createdAt,
    )

internal fun CommunityCommentDto.toCommunityComment(postId: Long): CommunityComment =
    CommunityComment(
        id = id,
        postId = postId,
        authorCohort = authorLabel.filter(Char::isDigit).toIntOrNull() ?: 0,
        content = content,
        createdAt = createdAt.toLocalDateTimeOrNow(),
        isMine = isMine,
        isEdited = updatedAt != createdAt,
    )

internal fun CommunityMyCommentDto.toCommunityMyComment(myCohort: Int): CommunityMyComment =
    CommunityMyComment(
        comment = CommunityComment(
            id = id,
            postId = postId,
            authorCohort = myCohort,
            content = content,
            createdAt = createdAt.toLocalDateTimeOrNow(),
            isMine = isMine,
        ),
        postCohort = null,
        postTitle = postTitle.orEmpty(),
    )

private fun String.toLocalDateTimeOrNow(): LocalDateTime =
    runCatching { OffsetDateTime.parse(this).toLocalDateTime() }
        .getOrElse { runCatching { LocalDateTime.parse(this) }.getOrDefault(LocalDateTime.now()) }
