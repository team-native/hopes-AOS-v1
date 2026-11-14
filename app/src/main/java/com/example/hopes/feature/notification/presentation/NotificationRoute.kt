package com.example.hopes.feature.notification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.hopes.R

/**
 * 알림 화면이다. 알림 서버 기능이 없어 피그마 시안의 예시 알림을 보여주고,
 * 편집·보기·모두 읽음 처리는 연동 전까지 동작하지 않는다.
 */
@Composable
fun NotificationRoute(onBackClick: () -> Unit) {
    val sampleItems =
        listOf(
            NotificationItem(
                id = 1,
                kind = NotificationKind.Answer,
                title = stringResource(R.string.notification_sample_answer_title),
                body = stringResource(R.string.notification_sample_answer_body),
            ),
            NotificationItem(
                id = 2,
                kind = NotificationKind.Profile,
                title = stringResource(R.string.notification_sample_profile_title),
                body = stringResource(R.string.notification_sample_profile_body),
            ),
            NotificationItem(
                id = 3,
                kind = NotificationKind.Admission,
                title = stringResource(R.string.notification_sample_admission_title),
                body = stringResource(R.string.notification_sample_admission_body),
            ),
            NotificationItem(
                id = 4,
                kind = NotificationKind.System,
                title = stringResource(R.string.notification_sample_system_title),
                body = stringResource(R.string.notification_sample_system_body),
            ),
        )

    NotificationScreen(
        items = sampleItems,
        onBackClick = onBackClick,
        onEditClick = {},
        onViewClick = {},
        onMarkAllReadClick = {},
    )
}
