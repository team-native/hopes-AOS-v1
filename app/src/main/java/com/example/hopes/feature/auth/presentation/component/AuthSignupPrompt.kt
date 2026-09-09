package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 로그인 폼 하단에서 회원가입 화면으로 유도하는 문구다. "회원가입" 부분만 강조 밑줄로 표시한다. */
@Composable
fun AuthSignupPrompt(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val accountPrompt = stringResource(R.string.no_account)
    val signupText = stringResource(R.string.signup)
    val accountPromptAnnotated = AnnotatedString.Builder().apply {
        append(accountPrompt.removeSuffix(signupText))
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(signupText)
        }
    }.toAnnotatedString()

    Text(
        text = accountPromptAnnotated,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = extendedColors.authFieldHint,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
    )
}
