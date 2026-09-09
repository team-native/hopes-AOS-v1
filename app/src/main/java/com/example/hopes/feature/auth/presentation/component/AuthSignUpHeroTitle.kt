package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 회원가입 화면 상단의 히어로 타이틀이다. */
@Composable
fun AuthSignUpHeroTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.signup_hero_title),
        modifier = modifier.width(260.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 35.sp),
    )
}
