package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * 빨간 아이콘 제목, 회색 설명, 밑줄 "다시 시도" 링크로 된 실패 안내다
 * (피그마 08-3 답변 생성 실패, 12 자료 분석 실패, 30-3 인기글 불러오기 실패).
 */
@Composable
fun HopesRetryMessage(
    title: String,
    description: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(AppIconSize.Small),
            )

            Spacer(modifier = Modifier.width(AppSpacing.Small + AppSpacing.XSmall))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.error,
                style = HopesTextStyles.BodyStrong,
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Subtitle,
        )

        Text(
            text = stringResource(R.string.retry),
            modifier =
                Modifier
                    .clickable(role = Role.Button, onClick = onRetryClick)
                    .padding(vertical = AppSpacing.Compact),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label.copy(textDecoration = TextDecoration.Underline),
        )
    }
}
