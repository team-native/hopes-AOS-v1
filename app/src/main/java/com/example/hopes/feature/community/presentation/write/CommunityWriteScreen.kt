package com.example.hopes.feature.community.presentation.write

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.designsystem.component.HopesErrorBanner
import com.example.hopes.core.designsystem.component.HopesInfoBanner
import com.example.hopes.core.designsystem.component.HopesInlineErrorMessage
import com.example.hopes.core.designsystem.component.HopesTextArea
import com.example.hopes.core.designsystem.component.HopesTextField
import com.example.hopes.core.designsystem.component.figmaStatusBarsPadding
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.feature.community.presentation.component.CommunityBoardSheet
import com.example.hopes.feature.community.presentation.component.communityBoardName
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 글쓰기·글 수정 화면이다(피그마 15 ~ 15-7). 게시판, 제목, 내용을 받고, 아래에 작성자 공개 범위를 안내한다.
 * 쓰던 내용이 있으면 닫을 때 확인을 묻는다.
 */
@Composable
fun CommunityWriteScreen(
    uiState: CommunityWriteUiState,
    onEvent: (CommunityWriteEvent) -> Unit,
) {
    BackHandler { onEvent(CommunityWriteEvent.CloseClicked) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .figmaStatusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
    ) {
        CommunityWriteHeader(
            title =
                stringResource(
                    if (uiState.isEditMode) R.string.community_edit_title else R.string.community_write_title,
                ),
            submitText =
                stringResource(
                    if (uiState.isEditMode) R.string.community_edit_submit else R.string.community_write_submit,
                ),
            isSubmitEnabled = !uiState.isSubmitting,
            onCloseClick = { onEvent(CommunityWriteEvent.CloseClicked) },
            onSubmitClick = { onEvent(CommunityWriteEvent.SubmitClicked) },
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        start = AppSpacing.ScreenHorizontal,
                        end = AppSpacing.ScreenHorizontal,
                        top = AppSpacing.Compact,
                        bottom = AppSpacing.Item,
                    ),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
        ) {
            if (uiState.isSubmitFailed) {
                HopesErrorBanner(
                    message =
                        stringResource(
                            if (uiState.isEditMode) R.string.community_edit_failed else R.string.community_write_failed,
                        ),
                )
            }

            CommunityBoardField(
                cohort = uiState.cohort,
                onClick = { onEvent(CommunityWriteEvent.BoardClicked) },
            )

            val titleError = if (uiState.isTitleError) stringResource(R.string.community_write_title_error) else null
            val titlePlaceholder = stringResource(R.string.community_write_title_placeholder)
            HopesTextField(
                value = uiState.title,
                onValueChange = { onEvent(CommunityWriteEvent.TitleChanged(it)) },
                placeholder = titlePlaceholder,
                accessibilityLabel = titlePlaceholder,
                errorMessage = titleError,
            )
            if (titleError != null) {
                HopesInlineErrorMessage(message = titleError)
            }

            val contentError =
                if (uiState.isContentError) stringResource(R.string.community_write_content_error) else null
            val contentPlaceholder = stringResource(R.string.community_write_content_placeholder)
            HopesTextArea(
                value = uiState.content,
                onValueChange = { onEvent(CommunityWriteEvent.ContentChanged(it)) },
                placeholder = contentPlaceholder,
                accessibilityLabel = contentPlaceholder,
                errorMessage = contentError,
                modifier = Modifier.weight(1f),
            )
            if (contentError != null) {
                HopesInlineErrorMessage(message = contentError)
            }

            HopesInfoBanner(
                message = stringResource(R.string.community_write_privacy),
                icon = Icons.Outlined.Lock,
            )
        }
    }

    if (uiState.isBoardSheetVisible) {
        CommunityBoardSheet(
            cohorts = uiState.cohorts,
            selectedCohort = uiState.cohort,
            onSelect = { onEvent(CommunityWriteEvent.BoardSelected(it)) },
            onDismiss = { onEvent(CommunityWriteEvent.BoardSheetDismissed) },
        )
    }

    if (uiState.isExitDialogVisible) {
        HopesAlertDialog(
            title = stringResource(R.string.community_write_exit_title),
            message = stringResource(R.string.community_write_exit_message),
            dismissText = stringResource(R.string.community_write_exit_continue),
            confirmText = stringResource(R.string.community_write_exit_leave),
            onDismiss = { onEvent(CommunityWriteEvent.ExitDismissed) },
            onConfirm = { onEvent(CommunityWriteEvent.ExitConfirmed) },
            isConfirmDestructive = true,
        )
    }
}

/** 왼쪽 닫기(X), 제목, 오른쪽 파란 "등록"/"저장" 버튼이다. */
@Composable
private fun CommunityWriteHeader(
    title: String,
    submitText: String,
    isSubmitEnabled: Boolean,
    onCloseClick: () -> Unit,
    onSubmitClick: () -> Unit,
) {
    val submitShape = RoundedCornerShape(AppRadius.SmallButton)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(AppSize.TopBar + AppSpacing.Compact)
                .padding(start = AppSpacing.Compact + AppSpacing.Small, end = AppSpacing.ScreenHorizontal),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(AppSize.IconButton)
                    .shapeClickable(shape = CircleShape, role = Role.Button, onClick = onCloseClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = title,
            modifier =
                Modifier
                    .weight(1f)
                    .semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.TopBarTitle,
        )

        Box(
            modifier =
                Modifier
                    .height(AppSize.Chip)
                    .background(
                        color =
                            if (isSubmitEnabled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_ALPHA)
                            },
                        shape = submitShape,
                    ).shapeClickable(
                        shape = submitShape,
                        enabled = isSubmitEnabled,
                        role = Role.Button,
                        onClick = onSubmitClick,
                    ).padding(horizontal = AppSpacing.Item + AppSpacing.XSmall),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = submitText,
                color = MaterialTheme.colorScheme.onPrimary,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

/** "게시판 · 10기 ⌄" 줄이다. 누르면 게시판 선택 시트를 연다(피그마 15-5). */
@Composable
private fun CommunityBoardField(
    cohort: Int?,
    onClick: () -> Unit,
) {
    val fieldShape = RoundedCornerShape(AppRadius.Field)
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(AppSize.Field)
                .background(MaterialTheme.colorScheme.surface, fieldShape)
                .border(AppBorderWidth.Default, extendedColors.authFieldBorder, fieldShape)
                .shapeClickable(shape = fieldShape, role = Role.DropdownList, onClick = onClick)
                .padding(horizontal = AppSpacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.community_write_board),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )

        Text(
            text = communityBoardName(cohort),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.LabelStrong,
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(AppIconSize.Small),
        )
    }
}

private const val DISABLED_ALPHA = 0.4f

@Preview(showBackground = true, name = "15 글쓰기")
@Composable
private fun CommunityWritePreview() {
    HopesTheme {
        CommunityWriteScreen(
            uiState =
                CommunityWriteUiState(
                    cohort = 10,
                    title = "기숙사 와이파이 다들 잘 돼?",
                    content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서…",
                ),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "15-3 필수 입력 오류")
@Composable
private fun CommunityWriteErrorPreview() {
    HopesTheme {
        CommunityWriteScreen(
            uiState = CommunityWriteUiState(cohort = 10, isTitleError = true, isContentError = true),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, name = "15-7 등록 실패")
@Composable
private fun CommunityWriteFailedPreview() {
    HopesTheme {
        CommunityWriteScreen(
            uiState =
                CommunityWriteUiState(
                    cohort = 10,
                    title = "기숙사 와이파이 다들 잘 돼?",
                    content = "3층 끝방인데 저녁만 되면 와이파이가 너무 느려져서…",
                    isSubmitFailed = true,
                ),
            onEvent = {},
        )
    }
}
