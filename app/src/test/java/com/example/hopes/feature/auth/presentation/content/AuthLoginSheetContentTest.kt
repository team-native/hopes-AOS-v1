package com.example.hopes.feature.auth.presentation.content

import org.junit.Assert.assertEquals
import org.junit.Test

class AuthLoginSheetContentTest {
    @Test
    fun `시트 위치를 접힘부터 펼침까지의 진행률로 변환한다`() {
        assertEquals(
            0f,
            calculateLoginSheetExpansionProgress(
                sheetTopOffsetPx = 800f,
                expandedTopOffsetPx = 300f,
                dismissedTopOffsetPx = 800f,
            ),
        )
        assertEquals(
            0.5f,
            calculateLoginSheetExpansionProgress(
                sheetTopOffsetPx = 550f,
                expandedTopOffsetPx = 300f,
                dismissedTopOffsetPx = 800f,
            ),
        )
        assertEquals(
            1f,
            calculateLoginSheetExpansionProgress(
                sheetTopOffsetPx = 300f,
                expandedTopOffsetPx = 300f,
                dismissedTopOffsetPx = 800f,
            ),
        )
    }

    @Test
    fun `시트 위치가 드래그 범위를 벗어나면 진행률을 제한한다`() {
        assertEquals(
            1f,
            calculateLoginSheetExpansionProgress(
                sheetTopOffsetPx = 100f,
                expandedTopOffsetPx = 300f,
                dismissedTopOffsetPx = 800f,
            ),
        )
        assertEquals(
            0f,
            calculateLoginSheetExpansionProgress(
                sheetTopOffsetPx = 900f,
                expandedTopOffsetPx = 300f,
                dismissedTopOffsetPx = 800f,
            ),
        )
    }
}
