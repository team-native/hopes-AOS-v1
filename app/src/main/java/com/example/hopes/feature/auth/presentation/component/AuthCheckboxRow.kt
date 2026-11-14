package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

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
        // 행 전체가 터치 영역이므로 체크박스 자체의 48dp 최소 터치 영역은 끈다.
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null,
                modifier = Modifier.size(AppIconSize.Medium),
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = extendedColors.authFieldBorder,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            )
        }

        Spacer(modifier = Modifier.width(AppSpacing.Compact))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Label,
        )
    }
}
