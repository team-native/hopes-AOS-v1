package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSize

/**
 * 피그마 화면은 높이 62의 iOS 상태바 아래에서 콘텐츠가 시작한다. Android 상태바는 보통 24 안팎으로 더 낮아
 * `statusBarsPadding()`만 쓰면 모든 요소가 피그마보다 위로 붙는다.
 *
 * 상단 여백을 상태바 높이와 피그마 상태바 높이(62) 중 큰 값으로 잡아, 상태바가 낮은 기기에서도 콘텐츠가 피그마와 같은
 * 위치에서 시작하고 상태바가 더 높은 기기(펀치홀 등)에서는 상태바를 가리지 않는다.
 *
 * @param lift 뒤로가기 줄이 있는 화면처럼 시작점을 피그마(62)보다 위로 당길 만큼이다. 상태바 높이보다는 올라가지 않는다.
 */
@Composable
fun Modifier.figmaStatusBarsPadding(lift: Dp = 0.dp): Modifier {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    return padding(top = maxOf(statusBarHeight, AppSize.FigmaStatusBar - lift))
}

/**
 * 이미 상태바 인셋을 처리하는 Scaffold 같은 컨테이너 위에 더해, 콘텐츠 시작점을 피그마(62)에 맞추는 추가 여백이다.
 * 상태바가 62 이상이면 0이다.
 */
@Composable
fun figmaStatusBarExtraPadding(): Dp {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    return (AppSize.FigmaStatusBar - statusBarHeight).coerceAtLeast(0.dp)
}
