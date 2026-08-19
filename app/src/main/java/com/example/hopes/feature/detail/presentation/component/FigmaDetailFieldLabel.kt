package com.example.hopes.feature.detail.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 카드 입력값 위에 표시하는 Figma의 작은 필드 라벨이다. */
@Composable
fun FigmaDetailFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
    )
}
