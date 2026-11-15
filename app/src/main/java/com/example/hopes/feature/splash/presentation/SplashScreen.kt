package com.example.hopes.feature.splash.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.figmaStatusBarsPadding
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R
import kotlinx.coroutines.delay

/**
 * 피그마 00 스플래시다. 흰 타일 로고가 작게 시작해 스프링으로 커지며 나타나고(scale 0.4→1, opacity 0→1),
 * 이어서 "hopes" 글자가 아래에서 올라오며 나타난다.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val logoScale = remember { Animatable(LOGO_START_SCALE) }
    val logoAlpha = remember { Animatable(0f) }
    val wordmarkAlpha = remember { Animatable(0f) }
    val wordmarkOffset = remember { Animatable(WORDMARK_START_OFFSET) }

    LaunchedEffect(Unit) {
        // 피그마 Motion/Splash Logo: Spring(stiffness 390, damping 18) ≈ 감쇠비 0.46, 약 0.8초.
        logoAlpha.animateTo(1f, tween(durationMillis = 200))
    }
    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = LOGO_DAMPING_RATIO, stiffness = LOGO_STIFFNESS),
        )
    }
    LaunchedEffect(Unit) {
        delay(WORDMARK_DELAY_MILLIS)
        wordmarkAlpha.animateTo(1f, tween(durationMillis = WORDMARK_DURATION_MILLIS))
    }
    LaunchedEffect(Unit) {
        delay(WORDMARK_DELAY_MILLIS)
        wordmarkOffset.animateTo(0f, tween(durationMillis = WORDMARK_DURATION_MILLIS))
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
    ) {
        val unit = maxWidth / DESIGN_SCREEN_WIDTH

        // 피그마 Decor / Cloud: 폭 402 기준 왼쪽 52에서 시작하고 오른쪽·아래로 화면을 넘어간다.
        Image(
            painter = painterResource(R.drawable.decor_cloud_splash),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = unit * CLOUD_LEFT, y = CLOUD_BOTTOM_OVERFLOW)
                    .requiredSize(width = unit * CLOUD_WIDTH, height = unit * CLOUD_HEIGHT),
        )

        Column(
            modifier = Modifier.fillMaxSize().figmaStatusBarsPadding().navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                SplashLogo(
                    scale = logoScale.value,
                    alpha = logoAlpha.value,
                    wordmarkAlpha = wordmarkAlpha.value,
                    wordmarkOffsetPx = wordmarkOffset.value,
                )

                Spacer(modifier = Modifier.height(16.dp))

                SplashTagline()
            }

            Text(
                text = stringResource(R.string.splash_school_name),
                color = LocalHopesExtendedColors.current.authFieldHint,
                style = HopesTextStyles.Caption,
                modifier = Modifier.padding(bottom = 24.dp),
            )
        }
    }
}

/** 180×180 영역 안에 96dp 로고 타일과 "hopes" 글자를 놓는다(피그마 Motion/Splash Logo). */
@Composable
private fun SplashLogo(
    scale: Float,
    alpha: Float,
    wordmarkAlpha: Float,
    wordmarkOffsetPx: Float,
) {
    val tileShape = RoundedCornerShape(21.dp)

    Box(modifier = Modifier.size(180.dp)) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 10.dp)
                    .size(96.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }.figmaRaisedShadow(tileShape)
                    .background(MaterialTheme.colorScheme.surface, tileShape)
                    .border(1.dp, LocalHopesExtendedColors.current.authFieldBorder, tileShape),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo_mark),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 28.dp, y = 17.dp)
                        .size(width = 38.dp, height = 60.dp),
            )
        }

        Text(
            text = stringResource(R.string.splash_wordmark),
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.Title,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 122.dp)
                    .graphicsLayer {
                        this.alpha = wordmarkAlpha
                        translationY = wordmarkOffsetPx
                    },
        )
    }
}

/** "선배에게 묻는 가장 솔직한 학교 이야기" 한 줄이다. 강조 구간만 브랜드 색으로 표시한다. */
@Composable
private fun SplashTagline() {
    val brand = MaterialTheme.colorScheme.primary
    val tagline =
        buildAnnotatedString {
            withStyle(SpanStyle(color = brand)) { append(stringResource(R.string.auth_hero_highlight_senior)) }
            append(stringResource(R.string.auth_hero_first_line_suffix))
            append(" ")
            withStyle(SpanStyle(color = brand)) { append(stringResource(R.string.auth_hero_second_line)) }
            append(" ")
            append(stringResource(R.string.auth_hero_third_line))
        }

    Text(
        text = tagline,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = HopesTextStyles.Label,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}

// 피그마 00 스플래시의 화면 폭, 구름 에셋 크기·위치다.
private const val DESIGN_SCREEN_WIDTH = 402f
private const val CLOUD_WIDTH = 454f
private const val CLOUD_HEIGHT = 234f
private const val CLOUD_LEFT = 52f

// 구름 에셋(위 659 + 높이 234 = 893)이 화면 높이 874를 넘어가는 만큼이다.
private val CLOUD_BOTTOM_OVERFLOW = 19.dp

// 피그마 Motion/Splash Logo: scale 0.4에서 시작, Spring(stiffness 390, damping 18).
private const val LOGO_START_SCALE = 0.4f
private const val LOGO_STIFFNESS = 390f
private const val LOGO_DAMPING_RATIO = 0.456f

// "hopes" 글자는 140→122(18) 아래에서 올라오며 나타난다.
private const val WORDMARK_START_OFFSET = 18f
private const val WORDMARK_DELAY_MILLIS = 350L
private const val WORDMARK_DURATION_MILLIS = 450

@Preview(showBackground = true, widthDp = 402, heightDp = 874)
@Composable
private fun SplashScreenPreview() {
    HopesTheme {
        SplashScreen()
    }
}
