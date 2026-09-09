package com.example.hopes.feature.history.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/** 하나의 지난 질문을 터치 가능한 기록 목록 항목으로 표시한다. */
@Composable
fun HistoryItem(
    question: String,
    onClick: () -> Unit,
) {
    Text(
        text = question,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { role = Role.Button }
            .clickable(onClick = onClick)
            .padding(vertical = HISTORY_ITEM_VERTICAL_PADDING),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyMedium,
    )
}

private val HISTORY_ITEM_VERTICAL_PADDING = 14.dp
