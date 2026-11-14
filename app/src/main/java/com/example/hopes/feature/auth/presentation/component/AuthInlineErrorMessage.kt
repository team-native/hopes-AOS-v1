package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 입력 필드 바로 아래에 붙는 아이콘 + 빨간 오류 문구다. 로그인·회원가입 화면이 함께 쓴다. */
@Composable
fun AuthInlineErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 바로 옆 문구가 같은 의미를 전달하므로 아이콘은 장식으로 취급한다.
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(14.dp),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style =
                TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                ),
        )
    }
}
