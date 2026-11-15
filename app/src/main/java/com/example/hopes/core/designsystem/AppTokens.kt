package com.example.hopes.core.designsystem

import androidx.compose.ui.unit.dp

/** UI 데모에서 반복되는 여백, 모서리, 아이콘 크기를 일관되게 제공한다. */
object AppSpacing {
    val ScreenHorizontal = 20.dp
    val ScreenVertical = 20.dp

    /** 피그마 Content의 위쪽 여백(상태바 아래 62 지점부터 8)이다. 상태바 높이는 figmaStatusBarsPadding이 맞춘다. */
    val SystemBarToContent = 8.dp
    val Section = 24.dp
    val Item = 12.dp
    val Compact = 8.dp
    val Small = 4.dp
    val XSmall = 2.dp
    val Medium = 16.dp
    val Large = 32.dp

    /** 화면 맨 아래 요소(버튼·링크)와 시스템 바(제스처 바) 사이 간격이다. 모든 화면이 같은 값을 쓴다. */
    val ScreenBottom = 12.dp
}

object AppRadius {
    val Card = 20.dp

    /** 피그마 홈 히어로 카드 모서리다. */
    val Hero = 24.dp

    /** 피그마 radius-lg: 통계·목록 카드 모서리다. */
    val Panel = 24.dp

    /** 피그마 radius-xl: 마이페이지 프로필 카드 모서리다. */
    val Profile = 28.dp

    /** 피그마 radius-sm: 목록 줄 아이콘 칩 모서리다. */
    val IconChip = 12.dp
    val ListCard = 20.dp
    val Button = 12.dp

    /** 피그마 하단 탭 한 칸의 선택 배경 모서리(radius-md)다. */
    val Tab = 16.dp
    val Logo = 14.dp
    val Field = 16.dp
    val Sheet = 28.dp
    val SmallButton = 10.dp
    val HeaderButton = 14.dp
}

object AppIconSize {
    /** 피그마 하단 탭 아이콘 크기다. */
    val BottomNavigation = 22.dp
    val Logo = 40.dp

    /** 오류·타이머처럼 12~13sp 문구 옆에 붙는 아이콘이다. */
    val Inline = 14.dp
    val Small = 16.dp
    val Medium = 20.dp
    val Large = 22.dp
}

/** 입력란, 버튼, 상단 바처럼 반복되는 컴포넌트의 고정 크기다. */
object AppSize {
    val Field = 54.dp
    val ListItem = 48.dp
    val Button = 50.dp
    val SmallButton = 36.dp
    val Chip = 32.dp
    val TopBar = 56.dp

    /** 하단 탭 줄 높이다. 피그마 하단바 86에서 홈 인디케이터 영역을 뺀 값(위 10 + 탭 54)이다. */
    val BottomNavigation = 64.dp
    val IconButton = 40.dp

    /** 피그마 iOS 상태바 높이다. 콘텐츠는 이 아래에서 시작한다. */
    val FigmaStatusBar = 62.dp

    /** 피그마 홈 알림 같은 헤더 아이콘 버튼 크기다. */
    val HeaderButton = 44.dp
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
