package com.example.hopes.feature.chat.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSecondaryButton
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.feature.chat.view.label
import com.example.hopes.feature.chat.viewmodel.ChatDepartment
import com.example.hopes.feature.chat.viewmodel.ChatMajor
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 학과 선택 시트다(피그마 06). 항목을 고른 뒤 "완료"를 눌러야 반영되고, "선택 해제"는 학과를 비운다.
 */
@Composable
fun ChatDepartmentSheet(
    selectedDepartment: ChatDepartment?,
    onConfirm: (ChatDepartment?) -> Unit,
    onDismiss: () -> Unit,
) {
    var pendingDepartment by rememberSaveable { mutableStateOf(selectedDepartment) }
    val extendedColors = LocalHopesExtendedColors.current

    ChatSelectionSheet(
        title = stringResource(R.string.chat_department_sheet_title),
        description = stringResource(R.string.chat_department_sheet_description),
        onClear = { onConfirm(null) },
        onDone = { onConfirm(pendingDepartment) },
        onDismiss = onDismiss,
    ) {
        Column(modifier = Modifier.selectableGroup()) {
            ChatDepartment.entries.forEach { department ->
                val isSelected = pendingDepartment == department

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(AppSize.ListItem)
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { pendingDepartment = department },
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = department.label(),
                        modifier = Modifier.weight(1f),
                        color =
                            if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                        style = if (isSelected) HopesTextStyles.FieldStrong else HopesTextStyles.Field,
                    )

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
    }
}

/**
 * 관심 전공 선택 시트다(피그마 07). 전공 칩을 하나 고른 뒤 "완료"를 눌러야 반영된다.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatMajorSheet(
    selectedMajor: ChatMajor?,
    onConfirm: (ChatMajor?) -> Unit,
    onDismiss: () -> Unit,
) {
    var pendingMajor by rememberSaveable { mutableStateOf(selectedMajor) }

    ChatSelectionSheet(
        title = stringResource(R.string.chat_major_sheet_title),
        description = stringResource(R.string.chat_major_sheet_description),
        onClear = { onConfirm(null) },
        onDone = { onConfirm(pendingMajor) },
        onDismiss = onDismiss,
    ) {
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact),
        ) {
            ChatMajor.entries.forEach { major ->
                ChatMajorChip(
                    text = major.label(),
                    isSelected = pendingMajor == major,
                    onClick = { pendingMajor = major },
                )
            }
        }
    }
}

@Composable
private fun ChatMajorChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .height(AppSize.Chip)
                .clip(CircleShape)
                .background(
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
                    role = Role.RadioButton,
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
            style = if (isSelected) HopesTextStyles.LabelStrong else HopesTextStyles.Label,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatSelectionSheet(
    title: String,
    description: String,
    onClear: () -> Unit,
    onDone: () -> Unit,
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
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
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

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            content()

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
                HopesSecondaryButton(
                    text = stringResource(R.string.chat_selection_clear),
                    onClick = onClear,
                    modifier = Modifier.weight(1f),
                )

                HopesPrimaryButton(
                    text = stringResource(R.string.chat_selection_done),
                    onClick = onDone,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }
    }
}

private const val SELECTED_BORDER_ALPHA = 0.4f
