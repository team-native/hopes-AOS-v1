package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing

/** hopes 서비스명을 표시하는 공통 브랜드 로고다. */
@Composable
fun HopesLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize.Logo)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(AppRadius.Logo),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.logo_mark),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.school_name),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/** 밝은 배경 위에서 사용하는 브랜드 로고를 제공한다. */
@Composable
fun HopesLightLogo() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        Box(
            modifier = Modifier
                .size(AppIconSize.Logo)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(AppRadius.Logo),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.logo_mark),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.school_name),
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/** 피그마의 채팅 시작 화면에서 사용하는 중앙 브랜드 마크다. */
@Composable
fun HopesLogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(74.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(AppRadius.Card),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.logo_mark),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}
