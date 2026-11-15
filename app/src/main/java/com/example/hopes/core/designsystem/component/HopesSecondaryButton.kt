package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.ui.theme.HopesTextStyles

/** 주요 버튼 옆에 두는 흰색 테두리 보조 버튼이다(예: "선택 해제"). 높이는 [HopesPrimaryButton]과 같다. */
@Composable
fun HopesSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val buttonShape = RoundedCornerShape(AppRadius.Button)

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(AppSize.Button)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = buttonShape,
                ).border(
                    width = AppBorderWidth.Default,
                    color = MaterialTheme.colorScheme.outline,
                    shape = buttonShape,
                ).shapeClickable(
                    shape = buttonShape,
                    enabled = isEnabled,
                    role = Role.Button,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color =
                if (isEnabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            style = HopesTextStyles.Button,
        )
    }
}
