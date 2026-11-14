package com.example.hopes.feature.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesHeaderAction
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

/** 알림 목록 한 줄이다. */
data class NotificationItem(
    val id: Long,
    val kind: NotificationKind,
    val title: String,
    val body: String,
)

/** 피그마 25 알림 화면이다. 알림 카드 아래에 "모두 읽음 처리" 버튼을 둔다. */
@Composable
fun NotificationScreen(
    items: List<NotificationItem>,
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
            HopesHeaderAction(
                text = stringResource(R.string.notification_edit),
                onClick = onEditClick,
            ),
    ) {
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
            onBackClick = {},
            onEditClick = {},
            onViewClick = {},
            onMarkAllReadClick = {},
        )
    }
}
