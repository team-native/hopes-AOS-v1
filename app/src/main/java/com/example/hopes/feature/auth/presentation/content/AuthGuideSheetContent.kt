package com.example.hopes.feature.auth.presentation.content

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.component.AuthHeroCopy
import com.example.hopes.feature.auth.presentation.component.AuthSheetHandle
import com.example.hopes.feature.auth.presentation.component.AuthSheetHeading
import com.example.hopes.feature.auth.presentation.component.AuthSwipeHint
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import kotlinx.coroutines.launch

/** 피그마 01 인증 안내 화면을 구성한다. 위로 스와이프하면 로그인 화면으로 이동한다. */
@Composable
fun AuthGuideSheetContent(onNavigateLogin: () -> Unit) {
    val animationScope = rememberCoroutineScope()
    val guideDensity = LocalDensity.current
    val extendedColors = LocalHopesExtendedColors.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 시트가 실제 사용 가능 높이와 무관하게 항상 peekHeight만 노출되도록, 접힌 상태의 top
        // 오프셋을 컨테이너의 실제 높이(maxHeight) 기준으로 계산한다. 피그마 프레임 고정 높이가
        // 아니라 실제 기기 높이만 쓰므로 화면 크기가 달라도 노출 높이가 항상 동일하게 유지된다.
        // expandedOffsetDp는 스와이프 완료 후 전환되는 AuthLoginSheetContent의 시작 오프셋
        // (expandedTopOffset = maxHeight.value - 502f)과 동일한 공식을 써서, 화면 전환 시
        // 시트 위치가 어긋나지 않도록 한다.
        val peekHeightDp = 190f
        val expandedOffsetDp = maxHeight.value - 502f
        val dismissedTopOffset = maxHeight.value - peekHeightDp
        val sheetTopOffset = remember(dismissedTopOffset) { Animatable(dismissedTopOffset) }

        // 상단 문구와 스와이프 안내는 드래그로 움직이지 않는 정적 레이아웃이므로 offset 대신
        // 일반 레이아웃 흐름(Column + weight Spacer)으로 배치한다. 기기 화면 비율이 피그마 01
        // 프레임과 달라 남는 세로 여백의 절대량이 기기마다 다르므로, 고정 dp 대신 피그마
        // 실측 간격 비율(설명 하단→화살표 상단 92px : 부제 하단→시트 상단 20px ≈ 23:5)로 남는
        // 공간을 나눠, 화면 크기가 달라져도 두 간격의 비율이 피그마 디자인과 일치하게 한다.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = peekHeightDp.dp),
        ) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 25.dp)) {
                FigmaAuthBrandHeader()

                Spacer(modifier = Modifier.height(74.dp))

                AuthHeroCopy()
            }

            Spacer(modifier = Modifier.weight(23f))

            // 조건문으로 넣고 빼면 sheetTopOffset이 바뀔 때마다(드래그 중 매 프레임)
            // 리컴포지션이 일어난다. graphicsLayer alpha로 옮기면 draw 단계에서만
            // 갱신되어 드래그 중에도 가볍다.
            AuthSwipeHint(
                extendedColors = extendedColors,
                modifier = Modifier.graphicsLayer {
                    alpha = if (sheetTopOffset.value > dismissedTopOffset - 164f) 1f else 0f
                },
            )

            Spacer(modifier = Modifier.weight(5f))
        }

        // 시트 위치는 드래그로 매 프레임 바뀌는 값이라, layout을 다시 계산하는 offset 대신
        // draw 단계에서만 이동시키는 graphicsLayer translationY를 쓴다. 리컴포지션·리레이아웃
        // 없이 위치만 갱신되어 드래그 중에도 더 가볍다. 값을 람다 안에서 읽어야 이 이점이 있다.
        FigmaAuthSheet(
            modifier = Modifier
                .graphicsLayer { translationY = sheetTopOffset.value.dp.toPx() }
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(guideDensity) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            animationScope.launch {
                                sheetTopOffset.snapTo(
                                    (sheetTopOffset.value + with(guideDensity) {
                                        dragAmount.y.toDp().value
                                    }).coerceIn(expandedOffsetDp, dismissedTopOffset),
                                )
                            }
                        },
                        onDragEnd = {
                            animationScope.launch {
                                if (sheetTopOffset.value < dismissedTopOffset - 144f) {
                                    sheetTopOffset.animateTo(expandedOffsetDp, tween(durationMillis = 180))
                                    onNavigateLogin()
                                } else {
                                    sheetTopOffset.animateTo(dismissedTopOffset, tween(durationMillis = 180))
                                }
                            }
                        },
                    )
                },
            isPeekSheet = true,
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            AuthSheetHandle()

            Spacer(modifier = Modifier.height(48.dp))

            AuthSheetHeading(
                title = stringResource(R.string.login),
                titleColor = MaterialTheme.colorScheme.onSurface,
                titleFontSize = 25.sp,
                subtitle = stringResource(R.string.login_guide_subtitle),
                subtitleColor = extendedColors.authSubtitle,
                subtitleFontSize = 12.sp,
            )
        }
    }
}
