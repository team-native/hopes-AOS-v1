package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 선택하면 입력창에 반영되는 추천 질문 목록이다. */
@Composable
fun ChatSuggestions(onSuggestionClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
        stringArrayResource(R.array.chat_suggestions).forEach { suggestion ->
            HopesSurfaceCard(
                modifier = Modifier.clickable {
                    onSuggestionClick(suggestion)
                },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(12.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.chat_suggestion_marker),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}
