package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 온보딩의 번호·설명 카드 구성요소다. */
@Composable
fun FigmaHomeTipCard(
    index: Int,
    topText: String,
    bottomText: String,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = modifier
            .width(338.dp)
            .height(78.dp)
            .figmaSubtleShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
            .padding(start = 14.dp, top = 14.dp),
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .background(extendedColors.onboardingStepContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = index.toString(),
                    color = extendedColors.onboardingStepText,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = topText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp,
                    ),
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = bottomText,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp),
                )
            }
        }
    }
}
