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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
        val peekHeightDp = 190f
        val expandedOffsetDp = 372f
        val dismissedTopOffset = maxHeight.value - peekHeightDp
        val sheetTopOffset = remember(dismissedTopOffset) { Animatable(dismissedTopOffset) }

        Column(modifier = Modifier.padding(start = 32.dp, top = 25.dp)) {
            FigmaAuthBrandHeader()

            Spacer(modifier = Modifier.height(74.dp))

            AuthHeroCopy()
        }

        if (sheetTopOffset.value > dismissedTopOffset - 164f) {
            AuthSwipeHint(extendedColors = extendedColors, sheetTopOffset = dismissedTopOffset)
        }

        // 시트 위치는 드래그로 계속 바뀌는 값이라 padding이 아닌 offset으로 적용한다. offset은
        // padding과 달리 음수 값에서도 예외를 던지지 않아, 작은 화면에서도 안전하다.
        FigmaAuthSheet(
            modifier = Modifier
                .offset(y = sheetTopOffset.value.dp)
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
