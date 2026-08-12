package com.example.hopes.feature.alert.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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

/** 피그마 알림 제목과 상단 편집 액션을 표시한다. */
@Composable
fun AlertHeader(
    isEditing: Boolean,
    onEditClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.alert_title),
        modifier = Modifier.offset(x = 24.dp, y = 76.dp),
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle(
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
        ),
    )
    Text(
        text = stringResource(R.string.alert_description),
        modifier = Modifier
            .offset(x = 24.dp, y = 111.dp)
            .width(260.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
        ),
    )
    Box(
        modifier = Modifier
            .offset(x = 324.dp, y = 76.dp)
            .width(54.dp)
            .height(36.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onEditClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(if (isEditing) R.string.done else R.string.edit),
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}
