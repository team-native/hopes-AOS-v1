package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing

/** 검색창과 목록 사이에 표시하는 "모든 기록" 섹션 라벨이다. */
@Composable
fun HistoryAllRecordsLabel() {
    Text(
        text = stringResource(R.string.history_all_records),
        modifier = Modifier.padding(
            start = AppSpacing.ScreenHorizontal,
            top = HISTORY_ALL_RECORDS_TOP_PADDING,
        ),
        fontSize = 12.sp,
        color = Color(0xFF6B7A94),
        fontWeight = FontWeight.W600
    )
}

// 검색창(top 157dp + height 41dp) 아래 8dp 지점이다.
internal val HISTORY_ALL_RECORDS_TOP_PADDING = 206.dp
