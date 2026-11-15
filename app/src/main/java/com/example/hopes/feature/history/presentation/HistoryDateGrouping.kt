package com.example.hopes.feature.history.presentation

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/** 지난 대화 목록의 묶음이다(피그마 24 "지난 7일", "이전"). */
enum class HistorySection {
    LastSevenDays,
    Earlier,
}

/**
 * 대화 행 아래 작은 회색 날짜 문구다(피그마 24-2 "어제", "2일 전").
 * 화면 문구는 view 계층이 정한다.
 */
sealed interface HistoryDateLabel {
    data object Today : HistoryDateLabel

    data object Yesterday : HistoryDateLabel

    data class DaysAgo(
        val days: Long,
    ) : HistoryDateLabel

    data class Date(
        val date: LocalDate,
    ) : HistoryDateLabel
}

/**
 * 서버가 준 updatedAt 문자열을 날짜로 바꾼다. ISO-8601의 Instant·OffsetDateTime·LocalDateTime·LocalDate 꼴을 받는다.
 * 읽을 수 없으면 null이라 그 대화는 "이전" 묶음에 날짜 없이 들어간다.
 */
fun parseHistoryDate(
    updatedAt: String,
    zoneId: ZoneId = ZoneId.systemDefault(),
): LocalDate? {
    val text = updatedAt.trim()
    return runCatching { OffsetDateTime.parse(text).atZoneSameInstant(zoneId).toLocalDate() }.getOrNull()
        ?: runCatching { Instant.parse(text).atZone(zoneId).toLocalDate() }.getOrNull()
        ?: runCatching { LocalDateTime.parse(text).toLocalDate() }.getOrNull()
        ?: runCatching { LocalDate.parse(text) }.getOrNull()
}

/** 오늘을 기준으로 7일 안(오늘 포함)이면 "지난 7일", 아니면 "이전"이다. */
fun historySectionOf(
    date: LocalDate?,
    today: LocalDate,
): HistorySection {
    if (date == null) return HistorySection.Earlier
    val days = ChronoUnit.DAYS.between(date, today)
    return if (days in 0 until LAST_SEVEN_DAYS) HistorySection.LastSevenDays else HistorySection.Earlier
}

fun historyDateLabelOf(
    date: LocalDate?,
    today: LocalDate,
): HistoryDateLabel? {
    if (date == null) return null
    return when (val days = ChronoUnit.DAYS.between(date, today)) {
        0L -> HistoryDateLabel.Today
        1L -> HistoryDateLabel.Yesterday
        in 2 until LAST_SEVEN_DAYS -> HistoryDateLabel.DaysAgo(days)
        else -> HistoryDateLabel.Date(date)
    }
}

private const val LAST_SEVEN_DAYS = 7L
