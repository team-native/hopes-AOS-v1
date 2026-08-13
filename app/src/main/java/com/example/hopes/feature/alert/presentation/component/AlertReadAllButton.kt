package com.example.hopes.feature.alert.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaSubtleShadow

/** 모든 알림을 로컬에서 읽음으로 바꾸는 피그마 하단 버튼이다. */
@Composable
fun AlertReadAllButton(
    isReadAll: Boolean,
    onClick: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .padding(start = 24.dp, top = 719.dp)
            .width(354.dp)
            .height(48.dp)
            .figmaSubtleShadow(buttonShape)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = buttonShape,
            )
            .clickable(enabled = !isReadAll, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                if (isReadAll) R.string.read_all_completed else R.string.read_all,
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}
