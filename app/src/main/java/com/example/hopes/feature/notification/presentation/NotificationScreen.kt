package com.example.hopes.feature.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesHeaderAction
import com.example.hopes.core.designsystem.component.HopesSkeleton
import com.example.hopes.core.designsystem.component.HopesSkeletonLine
import com.example.hopes.core.designsystem.component.HopesStatusMessage
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.notification.presentation.component.NotificationListCard
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 알림 종류다. 종류마다 목록 아이콘이 달라진다. */
enum class NotificationKind {
    Answer,
    Profile,
    Admission,
    System,
}

/**
 * 알림 한 개다.
 *
 * @param body 목록에 보이는 한 줄 요약이다.
 * @param detail 상세 화면(피그마 25-4) 본문이다.
 * @param date 상세 화면 제목 아래 날짜 문구다(예: "2026.11.12").
 */
data class NotificationItem(
    val id: Long,
    val kind: NotificationKind,
    val title: String,
    val body: String,
    val detail: String = body,
    val date: String = "",
)

/**
 * 피그마 25 알림 화면이다. 알림 카드 아래에 "모두 읽음 처리" 버튼을 둔다.
 * 불러오는 중에는 자리 표시를(25-2), 알림이 없으면 빈 화면 안내를(25-3) 보여준다.
 */
@Composable
fun NotificationScreen(
    items: List<NotificationItem>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onViewClick: (NotificationItem) -> Unit,
    onMarkAllReadClick: () -> Unit,
) {
    val markAllShape = RoundedCornerShape(AppRadius.ListCard)

    HopesSubScreenFrame(
        title = stringResource(R.string.notification_title),
        subtitle = stringResource(R.string.notification_subtitle),
        onBackClick = onBackClick,
        headerAction =
            if (items.isNotEmpty() && !isLoading) {
                HopesHeaderAction(
                    text = stringResource(R.string.notification_edit),
                    onClick = onEditClick,
                )
            } else {
                null
            },
    ) {
        if (isLoading) {
            NotificationLoadingCard()
            return@HopesSubScreenFrame
        }

        if (items.isEmpty()) {
            HopesStatusMessage(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.notification_empty_title),
                description = stringResource(R.string.notification_empty_description),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = EMPTY_TOP_PADDING),
            )
            return@HopesSubScreenFrame
        }

        NotificationListCard(
            items = items,
            onViewClick = onViewClick,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(AppSize.Field)
                    .figmaSubtleShadow(markAllShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = markAllShape,
                    ).shapeClickable(shape = markAllShape, role = Role.Button, onClick = onMarkAllReadClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.notification_mark_all_read),
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )
        }
    }
}

/** 알림을 불러오는 동안 목록 카드 자리에 그리는 회색 자리 표시다(피그마 25-2). */
@Composable
private fun NotificationLoadingCard() {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .background(MaterialTheme.colorScheme.surface, cardShape)
                .padding(AppSpacing.Medium),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium + AppSpacing.Small),
    ) {
        repeat(LOADING_ROW_COUNT) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                HopesSkeleton(
                    modifier = Modifier.size(LOADING_ICON_SIZE),
                    shape = RoundedCornerShape(AppRadius.SmallButton),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Item))

                Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Small + AppSpacing.XSmall)) {
                    HopesSkeletonLine(modifier = Modifier.fillMaxWidth(LONG_LINE_FRACTION))
                    HopesSkeletonLine(modifier = Modifier.fillMaxWidth(SHORT_LINE_FRACTION))
                }
            }
        }
    }
}

private val EMPTY_TOP_PADDING = 160.dp
private val LOADING_ICON_SIZE = 36.dp
private const val LOADING_ROW_COUNT = 4
private const val LONG_LINE_FRACTION = 0.8f
private const val SHORT_LINE_FRACTION = 0.5f

@Preview(showBackground = true, name = "25-2 알림 로딩")
@Composable
private fun NotificationScreenLoadingPreview() {
    HopesTheme {
        NotificationScreen(
            items = emptyList(),
            isLoading = true,
            onBackClick = {},
            onEditClick = {},
            onViewClick = {},
            onMarkAllReadClick = {},
        )
    }
}

@Preview(showBackground = true, name = "25-3 알림 빈 상태")
@Composable
private fun NotificationScreenEmptyPreview() {
    HopesTheme {
        NotificationScreen(
            items = emptyList(),
            isLoading = false,
            onBackClick = {},
            onEditClick = {},
            onViewClick = {},
            onMarkAllReadClick = {},
        )
    }
}

@Preview(showBackground = true, name = "25 알림")
@Composable
private fun NotificationScreenPreview() {
    HopesTheme {
        NotificationScreen(
            items =
                listOf(
                    NotificationItem(1, NotificationKind.Answer, "새 답변 도착", "기숙사 질문에 선배 답변이 도착했어요."),
                    NotificationItem(2, NotificationKind.Profile, "프로필 저장 완료", "개인화 정보가 반영됐어요."),
                    NotificationItem(3, NotificationKind.Admission, "입학 안내 업데이트", "면접 준비 질문이 추가됐어요."),
                    NotificationItem(4, NotificationKind.System, "시스템 공지", "답변 데이터 업데이트 완료"),
                ),
            isLoading = false,
            onBackClick = {},
            onEditClick = {},
            onViewClick = {},
            onMarkAllReadClick = {},
        )
    }
}
