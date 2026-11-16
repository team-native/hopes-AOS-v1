package com.example.hopes.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.ui.theme.HopesTextStyles

/** 입력란 위에 붙는 라벨이다(피그마 Field 라벨: Label 14/20, 보조 글자색). */
@Composable
fun HopesFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = HopesTextStyles.Label,
    )
}
