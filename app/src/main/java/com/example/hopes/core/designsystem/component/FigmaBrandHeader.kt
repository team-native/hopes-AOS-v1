package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 피그마 상단의 42dp 브랜드 마크와 두 줄 서비스명을 동일한 비율로 표시한다. */
@Composable
fun FigmaBrandHeader(
    modifier: Modifier = Modifier,
    isOnBlueBackground: Boolean = false,
) {
    val titleColor = if (isOnBlueBackground) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val subtitleColor = if (isOnBlueBackground) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Layout(
        modifier = modifier.width(250.dp),
        content = {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.logo_mark),
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                color = titleColor,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            )
            Text(
                text = stringResource(R.string.school_name),
                color = subtitleColor,
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium),
            )
        },
    ) { measurables, constraints ->
        val logo = measurables[0].measure(constraints)
        val title = measurables[1].measure(constraints)
        val subtitle = measurables[2].measure(constraints)
        layout(250.dp.roundToPx(), 42.dp.roundToPx()) {
            logo.placeRelative(0, 0)
            title.placeRelative(54.dp.roundToPx(), 0)
            subtitle.placeRelative(54.dp.roundToPx(), 23.dp.roundToPx())
        }
    }
}
