package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 로그인 실패 시 사용자에게 원인을 알리는 오류 문구다. 서버 메시지가 없으면 기본 문구로 대신한다. */
@Composable
fun AuthLoginErrorMessage(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message.ifBlank { stringResource(R.string.login_error_message) },
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 12.sp),
    )
}
