package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.hopes.R

/** 인증 화면(안내·로그인)이 공유하는 그라디언트 배경 이미지다. */
@Composable
fun AuthBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.login_guide_background),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}
