package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesBottomSheet
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesRadioOptionRow
import com.example.hopes.core.designsystem.component.HopesSheetHeader
import com.example.hopes.feature.community.model.CommunityReportReason
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 신고 사유를 하나 고르는 시트다(피그마 17). 사유를 골라야 "신고하기"가 켜진다. */
@Composable
fun CommunityReportSheet(
    onReport: (CommunityReportReason) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedReason by rememberSaveable { mutableStateOf<CommunityReportReason?>(null) }

    HopesBottomSheet(onDismiss = onDismiss) {
        HopesSheetHeader(
            title = stringResource(R.string.community_report_title),
            description = stringResource(R.string.community_report_description),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Column(modifier = Modifier.selectableGroup()) {
            CommunityReportReason.entries.forEachIndexed { index, reason ->
                if (index > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }

                HopesRadioOptionRow(
                    text = reason.label(),
                    isSelected = selectedReason == reason,
                    onClick = { selectedReason = reason },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        HopesPrimaryButton(
            text = stringResource(R.string.community_report),
            onClick = { selectedReason?.let(onReport) },
            modifier = Modifier.fillMaxWidth(),
            isEnabled = selectedReason != null,
        )
    }
}

/** 신고를 마친 뒤 띄우는 확인 시트다(피그마 17-2). */
@Composable
fun CommunityReportDoneSheet(onDismiss: () -> Unit) {
    val highlight = stringResource(R.string.community_report_done_highlight)
    val title = stringResource(R.string.community_report_done_title, highlight)
    val highlightColor = MaterialTheme.colorScheme.primary
    val highlightStart = title.indexOf(highlight)
    val annotatedTitle =
        buildAnnotatedString {
            if (highlightStart < 0) {
                append(title)
                return@buildAnnotatedString
            }
            append(title.substring(0, highlightStart))
            withStyle(SpanStyle(color = highlightColor)) { append(highlight) }
            append(title.substring(highlightStart + highlight.length))
        }

    HopesBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(DONE_ICON_CIRCLE_SIZE)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(DONE_ICON_SIZE),
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            Text(
                text = annotatedTitle,
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.Headline,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            Text(
                text = stringResource(R.string.community_report_done_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.Subtitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            HopesPrimaryButton(
                text = stringResource(R.string.confirm),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * 글을 올릴 게시판을 고르는 시트다(피그마 15-5). 항목을 고른 뒤 "선택"을 눌러야 반영된다.
 *
 * @param selectedCohort 지금 고른 게시판이다. null이면 "전체"다.
 */
@Composable
fun CommunityBoardSheet(
    cohorts: List<Int>,
    selectedCohort: Int?,
    onSelect: (Int?) -> Unit,
    onDismiss: () -> Unit,
) {
    var pendingCohort by rememberSaveable { mutableStateOf(selectedCohort) }
    val options = listOf<Int?>(null) + cohorts

    HopesBottomSheet(onDismiss = onDismiss) {
        HopesSheetHeader(
            title = stringResource(R.string.community_board_sheet_title),
            description = stringResource(R.string.community_board_sheet_description),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        Column(modifier = Modifier.selectableGroup()) {
            options.forEach { cohort ->
                HopesRadioOptionRow(
                    text = communityBoardName(cohort),
                    isSelected = pendingCohort == cohort,
                    onClick = { pendingCohort = cohort },
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        HopesPrimaryButton(
            text = stringResource(R.string.community_select),
            onClick = { onSelect(pendingCohort) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val DONE_ICON_CIRCLE_SIZE = 56.dp
private val DONE_ICON_SIZE = 28.dp
