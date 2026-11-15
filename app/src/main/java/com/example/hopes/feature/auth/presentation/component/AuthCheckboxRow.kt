package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** "로그인 유지", 약관 동의처럼 체크박스와 문구를 묶은 행이다. 행 전체가 하나의 토글 영역이다. */
@Composable
fun AuthCheckboxRow(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier =
            modifier.toggleable(
                value = isChecked,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 피그마 체크박스: 20dp 정사각형, 모서리 6, 선택 시 브랜드 색 채움 + 14dp 체크 아이콘이다.
        // 행 전체가 터치 영역이므로 체크박스 자체는 터치를 받지 않는다.
        val checkboxShape = RoundedCornerShape(CHECKBOX_RADIUS)
        Box(
            modifier =
                Modifier
                    .size(AppIconSize.Medium)
                    .background(
                        color = if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = checkboxShape,
                    ).border(
                        width = if (isChecked) 0.dp else AppBorderWidth.Default,
                        color = extendedColors.authFieldBorder,
                        shape = checkboxShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            if (isChecked) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(CHECK_ICON_SIZE),
                )
            }
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Label,
        )
    }
}

private val CHECKBOX_RADIUS = 6.dp
private val CHECK_ICON_SIZE = 14.dp
