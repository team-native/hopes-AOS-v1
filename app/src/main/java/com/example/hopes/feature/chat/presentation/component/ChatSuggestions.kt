package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 선택하면 입력창에 반영되는 추천 질문 목록이다. */
@Composable
fun ChatSuggestions(onSuggestionClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
        stringArrayResource(R.array.chat_suggestions).forEach { suggestion ->
            HopesSurfaceCard(
                modifier = androidx.compose.ui.Modifier.clickable {
                    onSuggestionClick(suggestion)
                },
            ) {
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
