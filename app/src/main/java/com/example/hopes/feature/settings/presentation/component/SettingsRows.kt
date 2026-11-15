package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 설정 행들을 감싸는 흰색 둥근 카드다. 행 사이 구분선은 [SettingsDivider]로 넣는다. */
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardShape = RoundedCornerShape(AppRadius.Card)

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .figmaSubtleShadow(cardShape)
                .clip(cardShape)
                .background(MaterialTheme.colorScheme.surface)
                // 피그마 32 카드 위아래 여백이다.
                .padding(vertical = AppSpacing.Item),
        content = content,
    )
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = AppSpacing.Medium),
        color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA),
    )
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
    val extendedColors = LocalHopesExtendedColors.current

    SettingsRowLayout(
        icon = icon,
        title = title,
        description = description,
        // 피그마 32의 스위치 행은 이동 행보다 조금 더 높다(65).
        verticalPadding = AppSpacing.Item + AppSpacing.XSmall,
        modifier =
            Modifier.toggleable(
                value = isChecked,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            ),
    ) {
        Switch(
            checked = isChecked,
            // 행 전체가 toggleable이라 스위치 자체 클릭은 받지 않는다.
            onCheckedChange = null,
            colors =
                SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    uncheckedTrackColor = extendedColors.toggleTrackOff,
                    uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                    uncheckedBorderColor = Color.Transparent,
                ),
        )
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = HopesTextStyles.Caption,
                )
            }

            contentColor == null -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(AppIconSize.Medium),
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
    verticalPadding: Dp = AppSpacing.Item,
    trailing: @Composable RowScope.() -> Unit,
) {
    val accentColor = contentColor ?: MaterialTheme.colorScheme.primary

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.Medium, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ICON_CONTAINER_SIZE)
                    .background(
                        color = accentColor.copy(alpha = ICON_CONTAINER_ALPHA),
                        shape = RoundedCornerShape(AppRadius.SmallButton),
                    ),
            contentAlignment = Alignment.Center,
        ) {
            // 바로 옆 제목이 같은 의미를 전달하므로 아이콘은 장식으로 취급한다.
            Icon(
                painter = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(AppIconSize.Medium),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Item))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = contentColor ?: MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )

            if (description != null) {
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
private const val ICON_CONTAINER_ALPHA = 0.1f
private const val DIVIDER_ALPHA = 0.6f
