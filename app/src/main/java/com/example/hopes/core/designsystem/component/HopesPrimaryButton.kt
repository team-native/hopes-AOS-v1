package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
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
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.ui.theme.HopesTextStyles

/** 화면 하단의 46dp 전체 폭 주요 버튼이다. 비활성 시 반투명하게 표시한다. */
@Composable
fun HopesPrimaryButton(
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
                .figmaSheetShadow(buttonShape)
                .background(
                    color =
                        if (isEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary.copy(alpha = DISABLED_ALPHA)
                        },
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
            color = MaterialTheme.colorScheme.onPrimary,
            style = HopesTextStyles.Button,
        )
    }
}

private const val DISABLED_ALPHA = 0.38f
