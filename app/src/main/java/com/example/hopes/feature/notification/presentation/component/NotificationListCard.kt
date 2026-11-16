package com.example.hopes.feature.notification.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.notification.presentation.NotificationItem
import com.example.hopes.feature.notification.presentation.NotificationKind
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 알림을 아이콘·제목·내용·"보기" 링크 한 줄씩 구분선으로 이어 붙인 카드다(피그마 25). */
@Composable
fun NotificationListCard(
    items: List<NotificationItem>,
    onViewClick: (NotificationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardShape = RoundedCornerShape(AppRadius.Panel)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                // 피그마 List Card: 위아래 14, 좌우 17은 각 행이 가진다.
                .padding(vertical = CARD_PADDING_VERTICAL),
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = ROW_PADDING_HORIZONTAL),
                    color = LocalHopesExtendedColors.current.authFieldBorder,
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
                .padding(horizontal = ROW_PADDING_HORIZONTAL, vertical = ROW_PADDING_VERTICAL),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_CONTAINER_SIZE)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(AppRadius.IconChip),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            // 바로 옆 제목이 알림 종류를 설명하므로 아이콘은 장식으로 취급한다.
            Icon(
                painter = painterResource(item.kind.icon()),
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

            Spacer(modifier = Modifier.height(AppSpacing.XSmall))

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

@DrawableRes
private fun NotificationKind.icon(): Int =
    when (this) {
        NotificationKind.Answer -> R.drawable.ic_comment
        NotificationKind.Profile -> R.drawable.ic_user
        NotificationKind.Admission -> R.drawable.ic_file
        NotificationKind.System -> R.drawable.ic_bell
    }

// 피그마 Notification Row: 아이콘 칩 40, 행 위아래 14, 목록 카드 좌우 17.
private val ICON_CONTAINER_SIZE = 40.dp
private val ROW_PADDING_VERTICAL = 14.dp
private val ROW_PADDING_HORIZONTAL = 17.dp
private val CARD_PADDING_VERTICAL = 14.dp
