package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 개인 설정·문의 같은 설정 항목으로 이동하는 행이다. */
@Composable
fun FigmaSettingsRow(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 12.sp),
            )
        }

        Box(
            modifier = Modifier
                .width(48.dp)
                .height(32.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.open),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
            )
        }
    }
}
