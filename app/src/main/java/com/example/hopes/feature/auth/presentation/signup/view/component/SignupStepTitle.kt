package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 단계별 두 줄 제목이다. [highlight]와 일치하는 첫 구간만 primary 색으로 강조한다. */
@Composable
fun SignupStepTitle(
    text: String,
    highlight: String,
    modifier: Modifier = Modifier,
) {
    val highlightColor = MaterialTheme.colorScheme.primary
    val highlightStart = text.indexOf(highlight)
    val title =
        buildAnnotatedString {
            append(text)
            if (highlightStart >= 0) {
                addStyle(
                    style = SpanStyle(color = highlightColor),
                    start = highlightStart,
                    end = highlightStart + highlight.length,
                )
            }
        }

    Text(
        text = title,
        modifier = modifier.semantics { heading() },
        color = MaterialTheme.colorScheme.onSurface,
        style =
            TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
            ),
    )
}
