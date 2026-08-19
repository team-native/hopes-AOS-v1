package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 회원가입 카드 상단에 서버 오류 문구를 표시한다. */
@Composable
fun AuthSignupErrorBanner(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.generic_error_message),
        modifier = modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.error,
        style = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
    )
}
