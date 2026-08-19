package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 프로필·개인 설정·문의에서 공통으로 쓰는 여러 줄 입력 필드다. */
@Composable
fun FigmaDetailTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    height: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(306.dp)
            .height(height.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier
                    .padding(start = 16.dp, top = 18.dp)
                    .width(260.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 15.sp, lineHeight = 22.sp),
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 16.dp, top = 14.dp)
                .width(274.dp)
                .height((height - 28).dp),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp,
            ),
        )
    }
}
