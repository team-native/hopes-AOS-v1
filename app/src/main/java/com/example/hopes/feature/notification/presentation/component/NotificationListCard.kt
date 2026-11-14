package com.example.hopes.feature.notification.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.notification.presentation.NotificationItem
import com.example.hopes.feature.notification.presentation.NotificationKind
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 알림을 아이콘·제목·내용·"보기" 링크 한 줄씩 구분선으로 이어 붙인 카드다(피그마 25). */
@Composable
fun NotificationListCard(
    items: List<NotificationItem>,
    onViewClick: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = AppSpacing.Small),
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = AppSpacing.Medium),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
                )
            }

            NotificationRow(
                item = item,
                onViewClick = { onViewClick(item) },
            )
        }
    }
}

@Composable
private fun NotificationRow(
    item: NotificationItem,
    onViewClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(role = Role.Button, onClick = onViewClick)
                .padding(horizontal = AppSpacing.Medium, vertical = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_CONTAINER_SIZE)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(AppRadius.SmallButton),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            // 바로 옆 제목이 알림 종류를 설명하므로 아이콘은 장식으로 취급한다.
            Icon(
                imageVector = item.kind.icon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )

            Text(
                text = item.body,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = stringResource(R.string.notification_view),
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.LabelStrong,
        )
    }
}

private fun NotificationKind.icon(): ImageVector =
    when (this) {
        NotificationKind.Answer -> Icons.AutoMirrored.Outlined.Chat
        NotificationKind.Profile -> Icons.Outlined.Person
        NotificationKind.Admission -> Icons.Outlined.Description
        NotificationKind.System -> Icons.Outlined.Notifications
    }

private val ICON_CONTAINER_SIZE = 36.dp
private const val DIVIDER_ALPHA = 0.6f
