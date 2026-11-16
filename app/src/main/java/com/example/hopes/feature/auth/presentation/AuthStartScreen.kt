package com.example.hopes.feature.auth.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaStatusBarsPadding
import com.example.hopes.feature.auth.presentation.component.AuthHeroHeadline
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.component.AuthStartCloudIllustration
import com.example.hopes.feature.auth.presentation.component.AuthStartDescription
import com.example.hopes.feature.auth.presentation.component.AuthStartLogoBadge
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 01 로그인 안내(시작) 화면이다. 시작하기는 회원가입, 하단 링크는 로그인 단계로 이동한다.
 *
 * @param loginSheetHeight 로그인 바텀시트가 올라와 있을 때 시트 높이다. null이면 시트가 없다.
 *   시트가 있으면 로고·헤드라인을 시트 위 빈 공간 가운데로 올려 시트에 가려지지 않게 하고,
 *   시트 아래로 숨는 버튼·링크·구름은 서서히 지운다.
 */
@Composable
fun AuthStartScreen(
    onStartClick: () -> Unit,
    onLoginClick: () -> Unit,
    loginSheetHeight: Dp? = null,
) {
    val density = LocalDensity.current
    val isSheetShown = loginSheetHeight != null
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val navigationBarHeight = with(density) { WindowInsets.navigationBars.getBottom(density).toDp() }
    // 평소에는 하단 버튼·링크 영역(50+12+20+12)과 제스처 바 높이를, 시트가 있으면 시트 높이를 비워 둔다.
    val heroBottomReserve by animateDpAsState(
        targetValue = loginSheetHeight ?: (START_BOTTOM_AREA_HEIGHT + navigationBarHeight),
        label = "startHeroBottomReserve",
    )
    val bottomAreaAlpha by animateFloatAsState(
        targetValue = if (isSheetShown) 0f else 1f,
        label = "startBottomAreaAlpha",
    )
    // 키보드가 올라와 시트가 길어지면 올릴 공간이 없으므로 로고·헤드라인을 서서히 지운다.
    val heroAlpha by animateFloatAsState(
        targetValue = if (isSheetShown && isImeVisible) 0f else 1f,
        label = "startHeroAlpha",
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        AuthStartCloudIllustration(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = CLOUD_BOTTOM_OFFSET)
                    .graphicsLayer { alpha = bottomAreaAlpha },
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .figmaStatusBarsPadding()
                    .padding(
                        start = START_HORIZONTAL_PADDING,
                        end = START_HORIZONTAL_PADDING,
                        bottom = heroBottomReserve,
                    ).graphicsLayer { alpha = heroAlpha },
        ) {
            Spacer(modifier = Modifier.weight(HERO_TOP_WEIGHT))

            AuthStartLogoBadge()

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthHeroHeadline()

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            AuthStartDescription()

            Spacer(modifier = Modifier.weight(HERO_BOTTOM_WEIGHT))
        }

        if (bottomAreaAlpha > 0f) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = START_HORIZONTAL_PADDING)
                        .graphicsLayer { alpha = bottomAreaAlpha },
            ) {
                AuthPrimaryActionButton(
                    text = stringResource(R.string.auth_start_button),
                    isEnabled = true,
                    onClick = onStartClick,
                )

                Spacer(modifier = Modifier.height(AppSpacing.Item))

                AuthLinkPrompt(
                    promptText = stringResource(R.string.auth_has_account_prompt),
                    linkText = stringResource(R.string.login),
                    onLinkClick = onLoginClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                Spacer(modifier = Modifier.height(AppSpacing.ScreenBottom))
            }
        }
    }
}

// 피그마에서 헤드라인 블록은 화면 중앙보다 약간 위, 버튼은 하단에 붙는다.
private const val HERO_TOP_WEIGHT = 1f
private const val HERO_BOTTOM_WEIGHT = 1f
private val CLOUD_BOTTOM_OFFSET = 96.dp

// 피그마 01 Content 좌우 패딩이다(다른 화면의 20과 다르다).
private val START_HORIZONTAL_PADDING = 28.dp

// 하단 버튼 50 + 간격 12 + 링크 20 + 아래 여백 12다.
private val START_BOTTOM_AREA_HEIGHT = 94.dp

@Preview(showBackground = true)
@Composable
private fun AuthStartScreenPreview() {
    HopesTheme {
        AuthStartScreen(
            onStartClick = {},
            onLoginClick = {},
        )
    }
}
