package com.example.hopes.feature.alert.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.feature.alert.presentation.AlertNotificationUiModel

/** 피그마 72dp 알림 카드와 우측 액션을 표시한다. */
@Composable
fun AlertNotificationRow(
    notification: AlertNotificationUiModel,
    isReadAll: Boolean,
    modifier: Modifier = Modifier,
    onActionClick: () -> Unit,
) {
    val cardShape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .width(354.dp)
            .height(72.dp)
            .figmaSubtleShadow(cardShape)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = cardShape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = cardShape,
            )
            .alpha(if (isReadAll) 0.68f else 1f),
    ) {
        Text(
            text = stringResource(notification.titleRes),
            modifier = Modifier.padding(start = 20.dp, top = 16.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Text(
            text = stringResource(notification.descriptionRes),
            modifier = Modifier
                .padding(start = 20.dp, top = 41.dp)
                .width(210.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 12.sp),
        )
        Box(
            modifier = Modifier
                .padding(start = 278.dp, top = 20.dp)
                .width(56.dp)
                .height(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(14.dp),
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(14.dp),
                )
                .clickable(onClick = onActionClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
            text = stringResource(notification.actionLabelRes),
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
        }
    }
}
