package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles

/** "이미 계정이 있으신가요? 로그인"처럼 안내 문구 뒤에 파란 링크가 붙는 하단 문구다. 링크 부분만 누를 수 있다. */
@Composable
fun AuthLinkPrompt(
    promptText: String,
    linkText: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle = HopesTextStyles.Label

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = promptText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = textStyle,
        )

        Spacer(modifier = Modifier.width(AppSpacing.XSmall))

        Text(
            text = linkText,
            modifier =
                Modifier
                    .clickable(
                        role = Role.Button,
                        onClick = onLinkClick,
                    ).padding(
                        horizontal = AppSpacing.Small,
                        vertical = AppSpacing.Compact,
                    ),
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.LabelStrong,
        )
    }
}
