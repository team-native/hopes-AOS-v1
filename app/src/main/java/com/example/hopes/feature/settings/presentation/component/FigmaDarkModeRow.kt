package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 다크 모드를 켜고 끄는 설정 행이다. */
@Composable
fun FigmaDarkModeRow(
    isEnabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .figmaSubtleShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle)
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = stringResource(R.string.dark_mode),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = stringResource(R.string.dark_mode_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 12.sp),
            )
        }

        Box(
            modifier = Modifier
                .width(44.dp)
                .height(25.dp)
                .background(
                    if (isEnabled) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        extendedColors.toggleTrackOff
                    },
                    RoundedCornerShape(50.dp),
                ),
        ) {
            Box(
                modifier = Modifier
                    .padding(start = if (isEnabled) 12.dp else 3.dp, top = 3.dp)
                    .width(29.dp)
                    .height(19.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50.dp)),
            )
        }
    }
}
