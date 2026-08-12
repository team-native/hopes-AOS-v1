package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesScaffold
import com.example.hopes.feature.home.presentation.component.HomeHero
import com.example.hopes.feature.home.presentation.component.HomeTipCard
import com.example.hopes.navigation.HopesDestination

/** 홈의 소개와 질문 팁을 세로로 조합한다. */
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
                .padding(
                    horizontal = AppSpacing.ScreenHorizontal,
                    vertical = AppSpacing.ScreenVertical,
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Section),
        ) {
            HomeHero(onStartChatClick = onStartChatClick)
            HomeTipCard()
        }
    }
}
