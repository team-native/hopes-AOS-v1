package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 사용자가 제출한 질문과 데모 답변을 함께 보여 주는 카드다. */
@Composable
fun ChatAnswerCard(question: String) {
    HopesSurfaceCard {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.chat_demo_answer),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
