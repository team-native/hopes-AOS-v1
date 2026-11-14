package com.example.hopes.feature.community.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.community.presentation.component.CommunityCohortFilterRow
import com.example.hopes.feature.community.presentation.component.CommunityHeader
import com.example.hopes.feature.community.presentation.component.CommunityStatusMessage
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 게시글 목록 자리에 보여줄 상태다. 서버 연동 전이라 지금은 [Empty]만 쓴다. */
enum class CommunityContentState {
    Empty,
    LoadError,
}

/** 피그마 19 커뮤니티 화면이다. 제목·기수 필터 아래에 게시글 목록 또는 상태 안내를 그린다. */
@Composable
fun CommunityScreen(
    contentState: CommunityContentState,
    cohorts: List<Int>,
    selectedCohort: Int?,
    onCohortSelect: (Int?) -> Unit,
    onSearchClick: () -> Unit,
    onMyActivityClick: () -> Unit,
    onWriteClick: () -> Unit,
    onRetryClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Community,
        onNavigate = onNavigate,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(AppSpacing.SystemBarToContent))

            CommunityHeader(
                onSearchClick = onSearchClick,
                onMyActivityClick = onMyActivityClick,
                modifier = Modifier.padding(horizontal = AppSpacing.ScreenHorizontal),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            CommunityCohortFilterRow(
                cohorts = cohorts,
                selectedCohort = selectedCohort,
                onCohortSelect = onCohortSelect,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = AppSpacing.ScreenHorizontal),
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                when (contentState) {
                    CommunityContentState.Empty -> {
                        CommunityStatusMessage(
                            icon = Icons.AutoMirrored.Outlined.Chat,
                            title = stringResource(R.string.community_empty_title),
                            description = stringResource(R.string.community_empty_description),
                            actionText = stringResource(R.string.community_write),
                            onActionClick = onWriteClick,
                        )
                    }

                    CommunityContentState.LoadError -> {
                        CommunityStatusMessage(
                            icon = Icons.Outlined.ErrorOutline,
                            title = stringResource(R.string.community_load_error_title),
                            description = stringResource(R.string.community_load_error_description),
                            actionText = stringResource(R.string.community_retry),
                            onActionClick = onRetryClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "19 커뮤니티 빈 화면")
@Composable
private fun CommunityScreenEmptyPreview() {
    HopesTheme {
        CommunityScreen(
            contentState = CommunityContentState.Empty,
            cohorts = listOf(8, 9, 10, 11),
            selectedCohort = null,
            onCohortSelect = {},
            onSearchClick = {},
            onMyActivityClick = {},
            onWriteClick = {},
            onRetryClick = {},
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, name = "19-2 커뮤니티 불러오기 오류")
@Composable
private fun CommunityScreenLoadErrorPreview() {
    HopesTheme {
        CommunityScreen(
            contentState = CommunityContentState.LoadError,
            cohorts = listOf(8, 9, 10, 11),
            selectedCohort = null,
            onCohortSelect = {},
            onSearchClick = {},
            onMyActivityClick = {},
            onWriteClick = {},
            onRetryClick = {},
            onNavigate = {},
        )
    }
}
