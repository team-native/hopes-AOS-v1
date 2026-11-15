package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 아래에서 올라오는 흰 시트다. 뒤 화면을 흐리게 하고, 내용은 좌우 화면 여백 안에 그린다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HopesBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        ApplyDialogWindowBackgroundBlur(blurRadius = AppBlurRadius.DialogWindowBackdrop)

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = AppSpacing.ScreenHorizontal,
                        end = AppSpacing.ScreenHorizontal,
                        bottom = AppSpacing.Item,
                    ),
            content = content,
        )
    }
}

/** 시트 맨 위의 굵은 제목과 회색 설명이다. */
@Composable
fun HopesSheetHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.SheetTitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Subtitle,
        )
    }
}

/** 더보기 시트의 한 줄이다. [isDestructive]면 빨간 글자로 그린다(삭제하기, 신고하기). */
data class HopesSheetAction(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false,
)

/** 아이콘 + 글자 줄들과 맨 아래 "닫기" 버튼으로 된 더보기 시트다(피그마 14-2, 14-3, 14-7). */
@Composable
fun HopesActionSheet(
    actions: List<HopesSheetAction>,
    onDismiss: () -> Unit,
) {
    HopesBottomSheet(onDismiss = onDismiss) {
        actions.forEachIndexed { index, action ->
            if (index > 0) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            }

            val color =
                if (action.isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(AppSize.ListItem + AppSpacing.Small)
                        .clickable(role = Role.Button, onClick = action.onClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(AppIconSize.Medium),
                )

                Spacer(modifier = Modifier.width(AppSpacing.Item))

                Text(
                    text = action.text,
                    color = color,
                    style = HopesTextStyles.Field,
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        HopesSecondaryButton(
            text = stringResource(R.string.close),
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * 시트 안의 하나만 고르는 줄이다. 고르면 글자가 파래지고 오른쪽 빈 원이 파란 체크로 바뀐다(피그마 15-5, 17).
 * 부모에 `Modifier.selectableGroup()`을 붙여 쓴다.
 */
@Composable
fun HopesRadioOptionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(AppSize.ListItem)
                .selectable(
                    selected = isSelected,
                    role = Role.RadioButton,
                    onClick = onClick,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color =
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            style = if (isSelected) HopesTextStyles.FieldStrong else HopesTextStyles.Field,
        )

        Box(
            modifier = Modifier.size(AppIconSize.Large),
            contentAlignment = Alignment.Center,
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppIconSize.Large),
                )
            } else {
                Box(
                    modifier =
                        Modifier
                            .size(AppIconSize.Medium)
                            .border(
                                width = AppBorderWidth.Emphasized,
                                color = extendedColors.authFieldBorder,
                                shape = CircleShape,
                            ),
                )
            }
        }
    }
}
