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

/** "이미 계정이 있으신가요? 로그인"처럼 안내 문구 뒤에 파란 링크가 붙는 하단 문구다. 링크 부분만 누를 수 있다. */
@Composable
fun AuthLinkPrompt(
    promptText: String,
    linkText: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        TextStyle(
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = promptText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = textStyle,
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = linkText,
            modifier =
                Modifier
                    .clickable(
                        role = Role.Button,
                        onClick = onLinkClick,
                    ).padding(
                        horizontal = 4.dp,
                        vertical = 8.dp,
                    ),
            color = MaterialTheme.colorScheme.primary,
            style = textStyle.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}
