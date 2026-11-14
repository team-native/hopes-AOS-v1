package com.example.hopes.feature.history.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class HistoryDateGroupingTest {
    private val today = LocalDate.of(2026, 11, 14)
    private val seoul = ZoneId.of("Asia/Seoul")

    @Test
    fun parsesCommonServerDateFormats() {
        assertEquals(LocalDate.of(2026, 11, 13), parseHistoryDate("2026-11-13T21:10:00", seoul))
        assertEquals(LocalDate.of(2026, 11, 14), parseHistoryDate("2026-11-13T16:00:00Z", seoul))
        assertEquals(LocalDate.of(2026, 11, 13), parseHistoryDate("2026-11-13T10:00:00+09:00", seoul))
        assertEquals(LocalDate.of(2026, 11, 13), parseHistoryDate("2026-11-13", seoul))
        assertNull(parseHistoryDate("어제", seoul))
    }

    @Test
    fun withinSevenDays_isLastSevenDays() {
        assertEquals(HistorySection.LastSevenDays, historySectionOf(today, today))
        assertEquals(HistorySection.LastSevenDays, historySectionOf(today.minusDays(6), today))
        assertEquals(HistorySection.Earlier, historySectionOf(today.minusDays(7), today))
        assertEquals(HistorySection.Earlier, historySectionOf(null, today))
    }

    @Test
    fun dateLabels_followDistanceFromToday() {
        assertEquals(HistoryDateLabel.Today, historyDateLabelOf(today, today))
        assertEquals(HistoryDateLabel.Yesterday, historyDateLabelOf(today.minusDays(1), today))
        assertEquals(HistoryDateLabel.DaysAgo(3), historyDateLabelOf(today.minusDays(3), today))
        assertEquals(HistoryDateLabel.Date(today.minusDays(10)), historyDateLabelOf(today.minusDays(10), today))
        assertNull(historyDateLabelOf(null, today))
    }
}
