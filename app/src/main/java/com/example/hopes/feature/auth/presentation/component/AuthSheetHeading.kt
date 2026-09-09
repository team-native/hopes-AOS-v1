package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/** 인증 시트 상단의 제목·부제 쌍이다. 안내·로그인 시트가 각자의 타이포 스타일로 재사용한다. */
@Composable
fun AuthSheetHeading(
    title: String,
    titleColor: Color,
    titleFontSize: TextUnit,
    subtitle: String,
    subtitleColor: Color,
    subtitleFontSize: TextUnit,
    modifier: Modifier = Modifier,
    subtitleLineHeight: TextUnit = TextUnit.Unspecified,
    spacing: Dp = 8.dp,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = titleColor,
            style = TextStyle(
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(spacing))

        Text(
            text = subtitle,
            color = subtitleColor,
            style = TextStyle(
                fontSize = subtitleFontSize,
                lineHeight = subtitleLineHeight,
            ),
        )
    }
}
