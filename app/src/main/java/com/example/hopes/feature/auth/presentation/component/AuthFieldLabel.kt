package com.example.hopes.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 인증 화면 입력 필드 위에 붙는 12sp 라벨이다. */
@Composable
fun AuthFieldLabel(labelRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(labelRes),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
    )
}
