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

/** 피그마 온보딩의 구체적인 질문 작성 예시를 보여 주는 카드다. */
@Composable
fun HomeTipCard() {
    HopesSurfaceCard {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            Text(
                text = stringResource(R.string.home_tip_one),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.home_tip_two),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
