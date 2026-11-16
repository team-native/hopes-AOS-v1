package com.example.hopes.feature.chat.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 채팅 홈 상단의 "학과 · …", "전공 · …" 선택 칩과 오른쪽 "새 대화" 버튼 줄이다.
 *
 * @param departmentLabel 고른 학과 이름이다. null이면 회색 "학과 선택" 칩을 보여준다.
 */
@Composable
fun ChatContextChipRow(
    departmentLabel: String?,
    majorLabel: String?,
    onDepartmentClick: () -> Unit,
    onMajorClick: () -> Unit,
    onNewChatClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Compact - AppSpacing.XSmall),
        ) {
            ChatContextChip(
                text =
                    departmentLabel?.let { stringResource(R.string.chat_department_chip, it) }
                        ?: stringResource(R.string.chat_department_placeholder),
                isSelected = departmentLabel != null,
                onClick = onDepartmentClick,
            )

            ChatContextChip(
                text =
                    majorLabel?.let { stringResource(R.string.chat_major_chip, it) }
                        ?: stringResource(R.string.chat_major_placeholder),
                isSelected = majorLabel != null,
                onClick = onMajorClick,
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))
        val newChatShape = RoundedCornerShape(AppRadius.SmallButton)

        Box(
            modifier =
                Modifier
                    // 피그마 Button/Outline/S: 높이 38, 모서리 10, 테두리 1.
                    .height(NEW_CHAT_BUTTON_HEIGHT)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = newChatShape,
                    ).border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, newChatShape)
                    .shapeClickable(shape = newChatShape, role = Role.Button, onClick = onNewChatClick)
                    .padding(horizontal = NEW_CHAT_PADDING_HORIZONTAL),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_new_chat),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.Label,
            )
        }
    }
}

@Composable
private fun ChatContextChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .height(CHIP_HEIGHT)
                .background(
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                    shape = CircleShape,
                ).border(
                    width = AppBorderWidth.Default,
                    color =
                        if (isSelected) {
                            LocalHopesExtendedColors.current.authFieldFocusBorder
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    shape = CircleShape,
                ).shapeClickable(shape = CircleShape, role = Role.DropdownList, onClick = onClick)
                .padding(horizontal = CHIP_PADDING_HORIZONTAL),
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
            maxLines = 1,
            style = HopesTextStyles.Chip,
        )
    }
}

// 피그마 Chip: 높이 32(5+20+5+테두리 2), 좌우 10(+테두리 1). 새 대화 버튼은 38, 좌우 14(+테두리 1)다.
private val CHIP_HEIGHT = 32.dp
private val CHIP_PADDING_HORIZONTAL = 11.dp
private val NEW_CHAT_BUTTON_HEIGHT = 38.dp
private val NEW_CHAT_PADDING_HORIZONTAL = 15.dp
