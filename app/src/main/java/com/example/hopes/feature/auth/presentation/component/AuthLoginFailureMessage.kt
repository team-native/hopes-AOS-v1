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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamnative.hopes.R

/** 로그인 실패 시 비밀번호 필드 아래에 표시하는 아이콘 + 오류 문구다. 서버 메시지가 없으면 기본 문구를 쓴다. */
@Composable
fun AuthLoginFailureMessage(
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
            text = message.ifBlank { stringResource(R.string.auth_login_failure_message) },
            color = MaterialTheme.colorScheme.error,
            style =
                TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                ),
        )
    }
}
