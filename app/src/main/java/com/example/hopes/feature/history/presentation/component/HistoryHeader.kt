package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing

/** 기록 화면의 제목과 안내 문구를 표시한다. */
@Composable
fun HistoryHeader() {
    Column(
        modifier = Modifier.padding(
            start = AppSpacing.ScreenHorizontal,
            top = HISTORY_HEADER_TOP_PADDING,
        ),
    ) {
        Text(
            text = stringResource(R.string.history_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = stringResource(R.string.history_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

private val HISTORY_HEADER_TOP_PADDING = AppSpacing.SystemBarToContent
