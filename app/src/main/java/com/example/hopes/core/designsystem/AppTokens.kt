package com.example.hopes.core.designsystem

import androidx.compose.ui.unit.dp

/** UI 데모에서 반복되는 여백, 모서리, 아이콘 크기를 일관되게 제공한다. */
object AppSpacing {
    val ScreenHorizontal = 24.dp
    val ScreenVertical = 20.dp
    val SystemBarToContent = 15.dp
    val Section = 24.dp
    val Item = 12.dp
    val Compact = 8.dp
    val Small = 4.dp
    val Large = 32.dp
}

object AppRadius {
    val Card = 20.dp
    val Button = 14.dp
    val Logo = 14.dp
    val Field = 14.dp
    val Sheet = 28.dp
}

object AppIconSize {
    val BottomNavigation = 24.dp
    val Logo = 40.dp
}

/** 오버레이가 표시될 때 배경의 시각적 깊이를 표현하는 blur 반경이다. */
object AppBlurRadius {
    val OverlayBackdrop = 8.dp
    // Modifier.blur()가 아니라 Window.setBackgroundBlurRadius() 기반이라 렌더링 방식이 달라,
    // 같은 시각적 강도를 내려면 OverlayBackdrop보다 큰 값이 필요하다.
    val DialogWindowBackdrop = 24.dp
}
