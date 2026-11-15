package com.example.hopes.feature.community.model

import java.time.Duration
import java.time.LocalDateTime

/** 글·댓글 옆에 붙는 "방금", "10분 전", "어제" 같은 시간 문구 종류다. */
sealed interface CommunityTimeLabel {
    data object JustNow : CommunityTimeLabel

    data class MinutesAgo(
        val minutes: Long,
    ) : CommunityTimeLabel

    data class HoursAgo(
        val hours: Long,
    ) : CommunityTimeLabel

    data object Yesterday : CommunityTimeLabel

    data class Date(
        val month: Int,
        val day: Int,
    ) : CommunityTimeLabel
}

/**
 * [createdAt]을 [now] 기준 시간 문구로 바꾼다.
 * 1분 안은 "방금", 1시간 안은 "N분 전", 오늘 안은 "N시간 전", 전날은 "어제", 그보다 전은 "M월 d일"이다.
 */
fun communityTimeLabelOf(
    createdAt: LocalDateTime,
    now: LocalDateTime,
): CommunityTimeLabel {
    val elapsed = Duration.between(createdAt, now)
    val today = now.toLocalDate()
    val createdDate = createdAt.toLocalDate()

    return when {
        elapsed.toMinutes() < 1 -> CommunityTimeLabel.JustNow
        elapsed.toHours() < 1 -> CommunityTimeLabel.MinutesAgo(elapsed.toMinutes())
        createdDate == today -> CommunityTimeLabel.HoursAgo(elapsed.toHours())
        createdDate == today.minusDays(1) -> CommunityTimeLabel.Yesterday
        else -> CommunityTimeLabel.Date(createdAt.monthValue, createdAt.dayOfMonth)
    }
}
