package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** 하나의 지난 질문을 표시하는 기록 목록 항목이다. */
@Composable
fun HistoryItem(question: String, onClick: () -> Unit) {
    Text(
        text = question,
        modifier = Modifier.clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge,
    )
}
