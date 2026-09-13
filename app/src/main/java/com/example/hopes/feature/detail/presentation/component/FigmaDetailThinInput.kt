package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 프로필 이름에 사용하는 40dp 높이의 가변 폭 단일 행 입력 필드다. */
@Composable
fun FigmaDetailThinInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 15.sp),
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 10.dp, end = 16.dp)
                .height(24.dp),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}
