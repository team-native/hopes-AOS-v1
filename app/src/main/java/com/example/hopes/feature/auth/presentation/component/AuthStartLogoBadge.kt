package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.teamnative.hopes.R

/** 시작 화면 헤드라인 위의 흰색 둥근 사각형 로고 배지다. */
@Composable
fun AuthStartLogoBadge(modifier: Modifier = Modifier) {
    val badgeShape = RoundedCornerShape(AppRadius.Logo)

    Box(
        modifier =
            modifier
                .size(LOGO_BADGE_SIZE)
                .figmaRaisedShadow(badgeShape)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = badgeShape,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.hopes_logo),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(LOGO_MARK_SIZE),
        )
    }
}

private val LOGO_BADGE_SIZE = 56.dp
private val LOGO_MARK_SIZE = DpSize(width = 22.dp, height = 32.dp)
