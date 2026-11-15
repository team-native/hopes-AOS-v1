package com.example.hopes.feature.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 25-4 알림(공지) 상세 화면이다. 종류 태그, 제목, 날짜, 본문을 카드 하나에 보여준다. */
@Composable
fun NotificationDetailScreen(
    item: NotificationItem,
    onBackClick: () -> Unit,
) {
    val kindLabel = item.kind.label()

    HopesSubScreenFrame(
        title =
            if (item.kind == NotificationKind.System) {
                stringResource(R.string.notification_detail_title)
            } else {
                stringResource(R.string.notification_title)
            },
        subtitle = kindLabel,
        onBackClick = onBackClick,
    ) {
        HopesSurfaceCard {
            Text(
                text = kindLabel,
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        .padding(horizontal = AppSpacing.Compact, vertical = AppSpacing.XSmall + AppSpacing.XSmall),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.Tag,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            Text(
                text = item.title,
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.Headline,
            )

            if (item.date.isNotEmpty()) {
                Text(
                    text = item.date,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = AppSpacing.Item),
                color = MaterialTheme.colorScheme.outline,
            )

            Text(
                text = item.detail,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.Body.copy(lineHeight = HopesTextStyles.Description.lineHeight),
            )
        }
    }
}

/** 알림 상세 경로다. 예시 알림에서 id로 찾고, 없으면 바로 돌아간다. */
@Composable
fun NotificationDetailRoute(
    notificationId: Long,
    onBackClick: () -> Unit,
) {
    val item = sampleNotifications().firstOrNull { it.id == notificationId }
    if (item == null) {
        LaunchedEffect(notificationId) { onBackClick() }
        return
    }

    NotificationDetailScreen(item = item, onBackClick = onBackClick)
}

@Preview(showBackground = true, name = "25-4 공지 상세")
@Composable
private fun NotificationDetailScreenPreview() {
    HopesTheme {
        NotificationDetailScreen(
            item =
                NotificationItem(
                    id = 4,
                    kind = NotificationKind.System,
                    title = "답변 데이터 업데이트 완료",
                    body = "답변 데이터 업데이트 완료",
                    detail = "선배 답변 데이터가 업데이트됐어요.\n\n이용 중 불편한 점은 마이페이지 > 문의하기로 알려 주세요.",
                    date = "2026.11.12",
                ),
            onBackClick = {},
        )
    }
}
