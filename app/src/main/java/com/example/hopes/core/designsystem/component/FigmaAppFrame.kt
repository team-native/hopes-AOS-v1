package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.navigation.HopesDestination

/** 피그마 iPhone 17 Pro(402×874) 좌표계를 유지하는 앱 화면 프레임이다. */
@Composable
fun FigmaAppFrame(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    background: @Composable BoxScope.() -> Unit = {},
    contentBackgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {
    FigmaPhoneScreen(
        background = background,
        overlay = { viewportMetrics ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .width((FIGMA_PHONE_WIDTH * viewportMetrics.scale).dp)
                    .height((FIGMA_TAB_BAR_HEIGHT * viewportMetrics.scale).dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(FIGMA_PHONE_WIDTH.dp)
                        .height(FIGMA_TAB_BAR_HEIGHT.dp)
                        .graphicsLayer(
                            scaleX = viewportMetrics.scale,
                            scaleY = viewportMetrics.scale,
                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 1f),
                        ),
                ) {
                    FigmaBottomNavigation(
                        selectedDestination = selectedDestination,
                        onNavigate = onNavigate,
                    )
                }
            }
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

/** 피그마의 84dp 하단 탭 바와 활성 지시자를 제공한다. */
@Composable
fun FigmaBottomNavigation(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
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
            .height(FIGMA_TAB_BAR_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        destinations.forEachIndexed { index, destination ->
            val centerX = 55 + (index * 88)
            val isSelected = destination == selectedDestination
            Box(
                modifier = Modifier
                    .offset(x = (centerX - 30).dp, y = 12.dp)
                    .width(60.dp)
                    .height(48.dp)
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
                Text(
                    text = if (isSelected) "●" else "○",
                    modifier = Modifier.offset(y = 5.dp),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = stringResource(destination.labelResourceId()),
                    modifier = Modifier
                        .offset(y = 27.dp)
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
