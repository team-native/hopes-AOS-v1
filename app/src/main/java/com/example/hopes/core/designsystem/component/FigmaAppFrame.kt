package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 iPhone 17 Pro(402×874) 좌표계를 유지하는 앱 화면 프레임이다. */
@Composable
fun FigmaAppFrame(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    background: @Composable BoxScope.() -> Unit = {},
    contentBackgroundColor: Color = MaterialTheme.colorScheme.background,
    imeOverlay: @Composable BoxScope.(FigmaViewportMetrics) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0
    val navigationBarInset = with(density) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }

    FigmaPhoneScreen(
        background = {
            // 디자인 캔버스 바깥도 실제 화면 배경으로 채워 빈 띠가 생기지 않게 한다.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(contentBackgroundColor),
            )
            background()
        },
        overlay = { viewportMetrics ->
            if (!isImeVisible) {
                // enableEdgeToEdge 환경에서는 바텀바 배경을 시스템 제스처 영역까지 확장한다.
                // 아이콘·라벨은 Figma 60dp 콘텐츠 영역에만 두어 내비게이션 바 위로 올라간다.
                val designBottomInset = (navigationBarInset.value / viewportMetrics.scale).dp
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width((FIGMA_PHONE_WIDTH * viewportMetrics.scale).dp)
                        .height((FIGMA_TAB_BAR_HEIGHT * viewportMetrics.scale).dp + navigationBarInset),
                ) {
                    Box(
                        modifier = Modifier
                            .width(FIGMA_PHONE_WIDTH.dp)
                            .height(FIGMA_TAB_BAR_HEIGHT.dp + designBottomInset)
                            .graphicsLayer(
                                scaleX = viewportMetrics.scale,
                                scaleY = viewportMetrics.scale,
                                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f),
                            ),
                    ) {
                        FigmaBottomNavigation(
                            selectedDestination = selectedDestination,
                            onNavigate = onNavigate,
                            bottomInset = designBottomInset,
                        )
                    }
                }
            }

            imeOverlay(viewportMetrics)
        },
    ) {
        Box(
            modifier = Modifier
                .width(402.dp)
                .height(874.dp)
                .background(contentBackgroundColor),
        ) {
            content()
        }
    }
}

/**
 * 피그마의 하단 탭 중 앱이 담당하는 60dp 영역을 제공한다.
 * 남은 24dp는 실제 Android 제스처 내비게이션 영역이 같은 위치를 담당한다.
 */
@Composable
fun FigmaBottomNavigation(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    bottomInset: androidx.compose.ui.unit.Dp = 0.dp,
) {
    val destinations = listOf(
        HopesDestination.Home,
        HopesDestination.Chat,
        HopesDestination.History,
        HopesDestination.Settings,
    )

    Box(
        modifier = Modifier
            .width(402.dp)
            .height(FIGMA_TAB_BAR_HEIGHT.dp + bottomInset)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            // 외부 padding 대신 바텀바 내부 여백으로 제스처 영역을 확보한다.
            .padding(bottom = bottomInset),
    ) {
        destinations.forEachIndexed { index, destination ->
            val centerX = 55 + (index * 88)
            val isSelected = destination == selectedDestination
            val destinationLabel = stringResource(destination.labelResourceId())
            Box(
                modifier = Modifier
                    .padding(start = (centerX - 30).dp, top = 12.dp)
                    .width(60.dp)
                    .height(48.dp)
                    // 원본의 60×30 시각 pill은 유지하면서 탭 전체를 접근 가능한 터치 영역으로 제공한다.
                    .semantics {
                        role = Role.Tab
                        contentDescription = destinationLabel
                        selected = isSelected
                    }
                    .clickable { onNavigate(destination) },
                contentAlignment = Alignment.TopCenter,
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(30.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(15.dp),
                            ),
                    )
                }
                Icon(
                    painter = painterResource(destination.bottomNavigationIconResourceId()),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .width(AppIconSize.BottomNavigation)
                        .height(AppIconSize.BottomNavigation),
                    tint = if (isSelected) {
                        LocalHopesExtendedColors.current.bottomNavigationSelectedIcon
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
                Text(
                    text = stringResource(destination.labelResourceId()),
                    modifier = Modifier
                        .padding(top = 27.dp)
                        .fillMaxWidth(),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

private const val FIGMA_TAB_BAR_HEIGHT = 60f

private fun HopesDestination.labelResourceId() = when (this) {
    HopesDestination.Home -> R.string.navigation_home
    HopesDestination.Chat -> R.string.navigation_chat
    HopesDestination.History -> R.string.navigation_history
    HopesDestination.Settings -> R.string.navigation_settings
    else -> R.string.navigation_home
}
