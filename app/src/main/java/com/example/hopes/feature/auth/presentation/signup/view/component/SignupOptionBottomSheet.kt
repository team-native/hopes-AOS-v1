package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 과·기수 선택에 쓰는 단일 선택 BottomSheet다(피그마 03-10-2).
 * 항목을 눌러 고른 뒤 "선택"을 눌러야 확정되며, 그냥 닫으면 [onDismiss]만 호출된다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupOptionBottomSheet(
    title: String,
    options: List<String>,
    initialSelectedIndex: Int?,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val extendedColors = LocalHopesExtendedColors.current
    var pendingIndex by rememberSaveable { mutableStateOf(initialSelectedIndex) }

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

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Column(modifier = Modifier.selectableGroup()) {
                options.forEachIndexed { index, option ->
                    val isSelected = pendingIndex == index

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(AppSize.ListItem)
                                .selectable(
                                    selected = isSelected,
                                    role = Role.RadioButton,
                                    onClick = { pendingIndex = index },
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.weight(1f),
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                            style =
                                if (isSelected) {
                                    HopesTextStyles.FieldStrong
                                } else {
                                    HopesTextStyles.Field
                                },
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

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthPrimaryActionButton(
                text = stringResource(R.string.signup_sheet_select),
                isEnabled = pendingIndex != null,
                onClick = {
                    pendingIndex?.let(onConfirm)
                },
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }
    }
}
