package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 더 구체적인 질문을 유도하는 홈 안내 카드다. */
@Composable
fun HomeTipCard() {
    HopesSurfaceCard {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            Text(
                text = stringResource(R.string.home_tip_title),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.home_tip_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
