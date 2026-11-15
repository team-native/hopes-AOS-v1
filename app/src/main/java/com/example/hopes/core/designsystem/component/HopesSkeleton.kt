package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/** 내용을 불러오는 동안 글자·아이콘 자리에 놓는 회색 자리 표시다. 크기와 모양은 [modifier]와 [shape]로 정한다. */
@Composable
fun HopesSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    Box(
        modifier =
            modifier.background(
                color = MaterialTheme.colorScheme.outline.copy(alpha = SKELETON_ALPHA),
                shape = shape,
            ),
    )
}

/** 글자 한 줄 높이의 자리 표시다. */
@Composable
fun HopesSkeletonLine(modifier: Modifier = Modifier) {
    HopesSkeleton(modifier = modifier.height(SKELETON_LINE_HEIGHT))
}

private const val SKELETON_ALPHA = 0.5f
private val SKELETON_LINE_HEIGHT = 10.dp
