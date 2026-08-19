package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 로그인 폼 안에서 비밀번호 재설정 화면으로 이동하는 우측 정렬 링크다. */
@Composable
fun AuthForgotPasswordLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.forgot_password),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.End,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
    )
}
