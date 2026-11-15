package com.example.hopes.feature.community.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CommunityTimeLabelTest {
    private val now = LocalDateTime.of(2026, 11, 15, 15, 0)

    @Test
    fun `1분이 안 지났으면 방금이다`() {
        assertEquals(CommunityTimeLabel.JustNow, communityTimeLabelOf(now.minusSeconds(30), now))
    }

    @Test
    fun `1시간 안이면 분 단위다`() {
        assertEquals(CommunityTimeLabel.MinutesAgo(10), communityTimeLabelOf(now.minusMinutes(10), now))
    }

    @Test
    fun `오늘 안이면 시간 단위다`() {
        assertEquals(CommunityTimeLabel.HoursAgo(3), communityTimeLabelOf(now.minusHours(3), now))
    }

    @Test
    fun `전날이면 어제다`() {
        assertEquals(CommunityTimeLabel.Yesterday, communityTimeLabelOf(now.minusDays(1), now))
    }

    @Test
    fun `이틀 이상 지났으면 날짜다`() {
        assertEquals(CommunityTimeLabel.Date(11, 12), communityTimeLabelOf(now.minusDays(3), now))
    }
}
