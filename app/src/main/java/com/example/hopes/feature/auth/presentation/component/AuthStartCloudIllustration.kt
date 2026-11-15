package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamnative.hopes.R

/**
 * 시작 화면(피그마 01) 하단의 장식용 구름이다. 피그마 Decor / Cloud 에셋(420×220)을 쓴다.
 *
 * 피그마에서 구름은 화면 폭 402 기준 왼쪽 40에서 시작해 폭 420으로 오른쪽 가장자리를 넘어간다.
 * 폭 비율로 크기와 위치를 맞추고, 화면 밖으로 나간 부분은 잘린다.
 */
@Composable
fun AuthStartCloudIllustration(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val unit = maxWidth / DESIGN_SCREEN_WIDTH
        Box(
            modifier = Modifier.fillMaxWidth().height(unit * CLOUD_HEIGHT),
            contentAlignment = Alignment.TopStart,
        ) {
            Image(
                painter = painterResource(R.drawable.decor_cloud_start),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier =
                    Modifier
                        .offset(x = unit * CLOUD_LEFT)
                        .requiredSize(width = unit * CLOUD_WIDTH, height = unit * CLOUD_HEIGHT),
            )
        }
    }
}

// 피그마 01 화면 폭과 구름 에셋 크기·위치다.
private const val DESIGN_SCREEN_WIDTH = 402f
private const val CLOUD_WIDTH = 420f
private const val CLOUD_HEIGHT = 220f
private const val CLOUD_LEFT = 40f

@Preview(showBackground = true, widthDp = 402)
@Composable
private fun AuthStartCloudIllustrationPreview() {
    AuthStartCloudIllustration()
}
