package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/** "메일이 안 왔다면 스팸함을 확인해 주세요."처럼 입력란 아래에 붙는 작은 회색 안내 문구다. */
@Composable
fun SignupHintText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
    )
}
