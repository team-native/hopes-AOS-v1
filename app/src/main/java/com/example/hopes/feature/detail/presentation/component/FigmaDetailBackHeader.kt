package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.component.FigmaBackButton

/** 상세·개인 설정·문의 화면에서 공통으로 쓰는 뒤로가기 상단 바다. */
@Composable
fun FigmaDetailBackHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    backOffsetX: Int = 18,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = backOffsetX.dp, top = 74.dp, end = 24.dp)
                .height(39.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FigmaBackButton(
                onClick = onBackClick,
            )

            Spacer(modifier = Modifier.width(18.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                ),
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.width(12.dp))

                FigmaDetailTopAction(
                    text = actionText,
                    modifier = Modifier,
                    onClick = onActionClick,
                )
            }
        }

        Text(
            text = subtitle,
            modifier = Modifier.padding(start = 74.dp, top = 111.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
        )
    }
}
