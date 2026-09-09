package com.example.hopes.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 인증 하위 단계 화면의 성공·실패 피드백 문구다. */
@Composable
fun AuthStatusText(
    message: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        modifier = modifier,
        color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
    )
}
