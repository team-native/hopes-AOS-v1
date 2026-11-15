package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.community.model.CommunityReportReason
import com.example.hopes.feature.community.model.CommunityTimeLabel
import com.example.hopes.feature.community.model.communityTimeLabelOf
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R
import java.time.LocalDateTime

/** "10기" 또는 "전체" 게시판 이름이다. */
@Composable
fun communityBoardName(cohort: Int?): String =
    if (cohort == null) {
        stringResource(R.string.community_cohort_all)
    } else {
        stringResource(R.string.community_cohort, cohort)
    }

/** 작성자 표시다. 내가 쓴 글·댓글이면 "10기 (나)"가 된다. */
@Composable
fun communityAuthorName(
    authorCohort: Int,
    isMine: Boolean,
): String {
    val cohort = stringResource(R.string.community_cohort, authorCohort)
    return if (isMine) stringResource(R.string.community_author_me, cohort) else cohort
}

/** "10분 전", "어제" 같은 시간 문구다. */
@Composable
fun communityTimeText(
    createdAt: LocalDateTime,
    now: LocalDateTime,
): String =
    when (val label = communityTimeLabelOf(createdAt, now)) {
        CommunityTimeLabel.JustNow -> stringResource(R.string.community_time_just_now)
        is CommunityTimeLabel.MinutesAgo -> stringResource(R.string.community_time_minutes_ago, label.minutes.toInt())
        is CommunityTimeLabel.HoursAgo -> stringResource(R.string.community_time_hours_ago, label.hours.toInt())
        CommunityTimeLabel.Yesterday -> stringResource(R.string.community_time_yesterday)
        is CommunityTimeLabel.Date -> stringResource(R.string.community_time_date, label.month, label.day)
    }

@Composable
fun CommunityReportReason.label(): String =
    stringResource(
        when (this) {
            CommunityReportReason.Abuse -> R.string.community_report_reason_abuse
            CommunityReportReason.Spam -> R.string.community_report_reason_spam
            CommunityReportReason.PersonalInfo -> R.string.community_report_reason_personal_info
            CommunityReportReason.Inappropriate -> R.string.community_report_reason_inappropriate
            CommunityReportReason.Other -> R.string.community_report_reason_other
        },
    )

/** 글 위에 붙는 연한 파란 "10기" 태그다. */
@Composable
fun CommunityBoardTag(
    cohort: Int?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(AppSpacing.Small + AppSpacing.XSmall),
                ).padding(horizontal = AppSpacing.Compact, vertical = AppSpacing.Small),
    ) {
        Text(
            text = communityBoardName(cohort),
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.Tag,
        )
    }
}
