package com.example.hopes.feature.history.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 하나의 지난 질문을 표시하는 기록 목록 항목이다. */
@Composable
fun HistoryItem(question: String, onClick: () -> Unit) {
    HopesSurfaceCard(modifier = Modifier.clickable(onClick = onClick)) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
