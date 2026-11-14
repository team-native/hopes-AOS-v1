package com.example.hopes.feature.notification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.teamnative.hopes.R

/**
 * 알림 API 연동 전까지 보여줄 피그마 시안의 예시 알림이다. 목록과 상세 화면이 같이 쓴다.
 * 연동 시 이 함수 대신 서버 알림 목록을 쓴다.
 */
@Composable
fun sampleNotifications(): List<NotificationItem> =
    listOf(
        NotificationItem(
            id = 1,
            kind = NotificationKind.Answer,
            title = stringResource(R.string.notification_sample_answer_title),
            body = stringResource(R.string.notification_sample_answer_body),
            detail = stringResource(R.string.notification_sample_answer_detail),
            date = stringResource(R.string.notification_sample_system_date),
        ),
        NotificationItem(
            id = 2,
            kind = NotificationKind.Profile,
            title = stringResource(R.string.notification_sample_profile_title),
            body = stringResource(R.string.notification_sample_profile_body),
            detail = stringResource(R.string.notification_sample_profile_detail),
            date = stringResource(R.string.notification_sample_system_date),
        ),
        NotificationItem(
            id = 3,
            kind = NotificationKind.Admission,
            title = stringResource(R.string.notification_sample_admission_title),
            body = stringResource(R.string.notification_sample_admission_body),
            detail = stringResource(R.string.notification_sample_admission_detail),
            date = stringResource(R.string.notification_sample_system_date),
        ),
        NotificationItem(
            id = 4,
            kind = NotificationKind.System,
            title = stringResource(R.string.notification_sample_system_title),
            body = stringResource(R.string.notification_sample_system_body),
            detail = stringResource(R.string.notification_sample_system_detail),
            date = stringResource(R.string.notification_sample_system_date),
        ),
    )

@Composable
fun NotificationKind.label(): String =
    stringResource(
        when (this) {
            NotificationKind.Answer -> R.string.notification_kind_answer
            NotificationKind.Profile -> R.string.notification_kind_profile
            NotificationKind.Admission -> R.string.notification_kind_admission
            NotificationKind.System -> R.string.notification_kind_system
        },
    )
