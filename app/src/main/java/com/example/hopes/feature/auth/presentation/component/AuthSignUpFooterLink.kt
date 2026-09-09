package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 회원가입 화면 하단에서 로그인 화면으로 돌아가는 링크다. */
@Composable
fun AuthSignUpFooterLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.has_account),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = LocalHopesExtendedColors.current.authFieldHint,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
    )
}
