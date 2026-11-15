package com.example.hopes.feature.community.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/**
 * "전체, 8기, 9기…" 기수 필터 칩 한 줄이다. 칩이 화면보다 넓어지면 가로로 스크롤된다.
 *
 * @param selectedCohort 선택된 기수다. null이면 "전체"가 선택된다.
 */
@Composable
fun CommunityCohortFilterRow(
    cohorts: List<Int>,
    selectedCohort: Int?,
    onCohortSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        modifier =
            modifier
                .horizontalScroll(rememberScrollState())
                .padding(contentPadding)
                .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
    ) {
        CommunityFilterChip(
            text = stringResource(R.string.community_cohort_all),
            isSelected = selectedCohort == null,
            onClick = { onCohortSelect(null) },
        )

        cohorts.forEach { cohort ->
            CommunityFilterChip(
                text = stringResource(R.string.community_cohort, cohort),
                isSelected = selectedCohort == cohort,
                onClick = { onCohortSelect(cohort) },
            )
        }
    }
}

/** 고르면 연한 파란 배경이 되는 알약 모양 칩이다. 기수 필터와 내 활동 탭(피그마 18)에 쓴다. 부모에 `selectableGroup()`을 붙인다. */
@Composable
fun CommunityFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                // 피그마 13 기수 칩 높이(37)에 맞춘다.
                .height(AppSize.Chip + AppSpacing.Small)
                .clip(CircleShape)
                .background(
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                ).border(
                    width = AppBorderWidth.Default,
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = SELECTED_BORDER_ALPHA)
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    shape = CircleShape,
                ).selectable(
                    selected = isSelected,
                    role = Role.Tab,
                    onClick = onClick,
                ).padding(horizontal = AppSpacing.Item + AppSpacing.XSmall),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            style =
                if (isSelected) {
                    HopesTextStyles.LabelStrong
                } else {
                    HopesTextStyles.Label
                },
        )
    }
}

private const val SELECTED_BORDER_ALPHA = 0.4f
