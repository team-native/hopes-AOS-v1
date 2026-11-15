package com.example.hopes.feature.settings.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 설정 행들을 감싸는 흰색 둥근 카드다. 행 사이 구분선은 [SettingsDivider]로 넣는다. */
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = CARD_PADDING_HORIZONTAL,
    verticalPadding: Dp = CARD_PADDING_VERTICAL,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardShape = RoundedCornerShape(AppRadius.Panel)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(AppBorderWidth.Default, LocalHopesExtendedColors.current.authFieldBorder, cardShape)
                // 피그마 List Card: 안쪽 여백 좌우 17·위아래 14에 테두리 1을 더한 값이다. Logout Card는 좌우 16에 위아래 0이다.
                .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        content = content,
    )
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(color = LocalHopesExtendedColors.current.authFieldBorder)
}

/** 아이콘·제목·설명 오른쪽에 스위치를 둔 행이다. 행 전체를 눌러도 켜고 끌 수 있다. */
@Composable
fun SettingsToggleRow(
    icon: Painter,
    title: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsRowLayout(
        icon = icon,
        title = title,
        description = description,
        modifier =
            Modifier.toggleable(
                value = isChecked,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            ),
    ) {
        FigmaToggle(isChecked = isChecked)
    }
}

/**
 * 누르면 다른 화면으로 이동하는 행이다.
 *
 * @param trailingText 있으면 오른쪽에 회색 값(예: "v1.5")을, 없으면 오른쪽 화살표를 보여준다.
 * @param contentColor 로그아웃처럼 위험한 동작은 error 색을 넘긴다. null이면 기본 색을 쓴다.
 */
@Composable
fun SettingsNavigationRow(
    icon: Painter,
    title: String,
    onClick: () -> Unit,
    description: String? = null,
    trailingText: String? = null,
    contentColor: Color? = null,
) {
    SettingsRowLayout(
        icon = icon,
        title = title,
        description = description,
        contentColor = contentColor,
        modifier = Modifier.clickable(role = Role.Button, onClick = onClick),
    ) {
        when {
            trailingText != null -> {
                Text(
                    text = trailingText,
                    color = LocalHopesExtendedColors.current.authFieldHint,
                    style = HopesTextStyles.Label,
                )
            }

            contentColor == null -> {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                    tint = LocalHopesExtendedColors.current.authFieldHint,
                    modifier = Modifier.size(ROW_ICON_SIZE),
                )
            }
        }
    }
}

@Composable
private fun SettingsRowLayout(
    icon: Painter,
    title: String,
    description: String?,
    modifier: Modifier = Modifier,
    contentColor: Color? = null,
    trailing: @Composable RowScope.() -> Unit,
) {
    // 피그마 로그아웃 줄은 칩 없이 22dp 빨간 아이콘만 두고 위아래 14, 그 밖의 줄은 칩 36에 위아래 12다.
    val isDanger = contentColor != null
    val accentColor = contentColor ?: MaterialTheme.colorScheme.primary

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = if (isDanger) DANGER_ROW_PADDING_VERTICAL else AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isDanger) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(DANGER_ICON_SIZE),
            )
        } else {
            Box(
                modifier =
                    Modifier
                        .size(ICON_CONTAINER_SIZE)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(AppRadius.IconChip),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                // 바로 옆 제목이 같은 의미를 전달하므로 아이콘은 장식으로 취급한다.
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(ROW_ICON_SIZE),
                )
            }
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = contentColor ?: MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.Body,
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(AppSpacing.XSmall))
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }
        }

        trailing()
    }
}

private val ICON_CONTAINER_SIZE = 36.dp

// 피그마 List Card의 안쪽 여백(테두리 1 포함)과 아이콘 크기다.
private val CARD_PADDING_HORIZONTAL = 18.dp
private val CARD_PADDING_VERTICAL = 15.dp
private val ROW_ICON_SIZE = 18.dp
private val DANGER_ICON_SIZE = 22.dp
private val DANGER_ROW_PADDING_VERTICAL = 14.dp

/** 피그마 Toggle: 46×26 트랙(켜짐 브랜드, 꺼짐 #DCE6F4)과 지름 22의 흰 원이다. */
@Composable
private fun FigmaToggle(isChecked: Boolean) {
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) TOGGLE_WIDTH - TOGGLE_THUMB_SIZE - TOGGLE_THUMB_INSET else TOGGLE_THUMB_INSET,
        label = "toggleThumb",
    )
    Box(
        modifier =
            Modifier
                .size(width = TOGGLE_WIDTH, height = TOGGLE_HEIGHT)
                .background(
                    color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = thumbOffset)
                    .size(TOGGLE_THUMB_SIZE)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
        )
    }
}

private val TOGGLE_WIDTH = 46.dp
private val TOGGLE_HEIGHT = 26.dp
private val TOGGLE_THUMB_SIZE = 22.dp
private val TOGGLE_THUMB_INSET = 2.dp
