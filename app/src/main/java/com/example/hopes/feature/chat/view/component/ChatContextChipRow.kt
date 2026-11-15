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
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
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

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        val newChatShape = RoundedCornerShape(AppRadius.SmallButton)

        Box(
            modifier =
                Modifier
                    .height(AppSize.Chip)
                    .figmaSubtleShadow(newChatShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = newChatShape,
                    ).shapeClickable(shape = newChatShape, role = Role.Button, onClick = onNewChatClick)
                    .padding(horizontal = AppSpacing.Item),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.chat_new_chat),
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.LabelStrong,
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
                            MaterialTheme.colorScheme.primary.copy(alpha = SELECTED_BORDER_ALPHA)
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                    shape = CircleShape,
                ).shapeClickable(shape = CircleShape, role = Role.DropdownList, onClick = onClick)
                .padding(horizontal = AppSpacing.Item - AppSpacing.XSmall),
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
            style = HopesTextStyles.Tag,
        )
    }
}

private val CHIP_HEIGHT = AppSize.Chip - AppSpacing.Compact
private const val SELECTED_BORDER_ALPHA = 0.4f
