package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.home.presentation.component.HomeHero
import com.example.hopes.feature.home.presentation.component.HomeTipCard
import com.example.hopes.navigation.HopesDestination

/** 피그마 04 온보딩 프레임을 로그인 뒤 홈 탭의 첫 화면으로 표시한다. */
@Composable
fun HomeScreenContent(
    onStartChatClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    HopesScaffold(
        selectedDestination = HopesDestination.Home,
        onNavigate = onNavigate,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppSpacing.ScreenHorizontal),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            HomeHero(onStartChatClick = onStartChatClick)
            HomeTipCard()
            HomeTipCard()
        }
    }
}
