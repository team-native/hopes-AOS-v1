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
    val XSmall = 2.dp
    val Medium = 16.dp
    val Large = 32.dp
}

object AppRadius {
    val Card = 20.dp
    val ListCard = 16.dp
    val Button = 14.dp
    val Logo = 14.dp
    val Field = 14.dp
    val Sheet = 28.dp
    val SmallButton = 10.dp
    val HeaderButton = 12.dp
}

object AppIconSize {
    val BottomNavigation = 24.dp
    val Logo = 40.dp

    /** 오류·타이머처럼 12~13sp 문구 옆에 붙는 아이콘이다. */
    val Inline = 14.dp
    val Small = 16.dp
    val Medium = 20.dp
    val Large = 22.dp
}

/** 입력란, 버튼, 상단 바처럼 반복되는 컴포넌트의 고정 크기다. */
object AppSize {
    val Field = 48.dp
    val ListItem = 48.dp
    val Button = 46.dp
    val SmallButton = 36.dp
    val Chip = 32.dp
    val TopBar = 56.dp
    val BottomNavigation = 64.dp
    val IconButton = 40.dp
}

object AppBorderWidth {
    val Default = 1.dp

    /** 포커스·오류 상태에서 테두리를 강조할 때 쓴다. */
    val Emphasized = 1.5.dp
}

/** 오버레이가 표시될 때 배경의 시각적 깊이를 표현하는 blur 반경이다. */
object AppBlurRadius {
    val OverlayBackdrop = 8.dp

    // Modifier.blur()가 아니라 Window.setBackgroundBlurRadius() 기반이라 렌더링 방식이 달라,
    // 같은 시각적 강도를 내려면 OverlayBackdrop보다 큰 값이 필요하다.
    val DialogWindowBackdrop = 24.dp
}
