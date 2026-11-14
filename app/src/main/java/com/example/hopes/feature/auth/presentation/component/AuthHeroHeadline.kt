package com.example.hopes.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.teamnative.hopes.R

/** "선배에게 묻는 / 가장 솔직한 / 학교 이야기" 세 줄 헤드라인이다. 강조 구간만 primary 색으로 표시한다. */
@Composable
fun AuthHeroHeadline(modifier: Modifier = Modifier) {
    val highlightStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)
    val seniorText = stringResource(R.string.auth_hero_highlight_senior)
    val firstLineSuffix = stringResource(R.string.auth_hero_first_line_suffix)
    val secondLine = stringResource(R.string.auth_hero_second_line)
    val thirdLine = stringResource(R.string.auth_hero_third_line)
    val headline =
        buildAnnotatedString {
            withStyle(highlightStyle) {
                append(seniorText)
            }
            append(firstLineSuffix)
            append("\n")
            withStyle(highlightStyle) {
                append(secondLine)
            }
            append("\n")
            append(thirdLine)
        }

    Text(
        text = headline,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style =
            TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
            ),
    )
}
