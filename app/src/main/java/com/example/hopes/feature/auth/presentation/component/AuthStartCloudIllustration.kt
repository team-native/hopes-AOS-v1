package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.teamnative.hopes.R

/** 시작 화면 하단의 장식용 구름 일러스트다. */
@Composable
fun AuthStartCloudIllustration(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.auth_start_cloud),
        // 의미 없는 장식 이미지라 접근성 서비스가 읽지 않도록 한다.
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier,
    )
}
