package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles

/**
 * 홈 섹션 제목이다. [actionText]가 있으면 오른쪽에 "더보기" 같은 파란 링크를 붙인다.
 */
@Composable
fun HomeSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier =
                Modifier
                    .weight(1f)
                    .semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.SectionTitle,
        )

        if (actionText != null) {
            Text(
                text = actionText,
                modifier =
                    Modifier
                        .clickable(role = Role.Button, onClick = onActionClick)
                        .padding(horizontal = AppSpacing.Small, vertical = AppSpacing.Compact),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}
