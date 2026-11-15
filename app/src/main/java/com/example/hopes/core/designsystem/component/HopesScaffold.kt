package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.hopes.navigation.HopesDestination

/** 공통 하단 탐색을 포함한 화면 뼈대를 제공한다. */
@Composable
fun HopesScaffold(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    fixedTopContent: (@Composable () -> Unit)? = null,
    fixedBottomContent: (@Composable () -> Unit)? = null,
    isBottomNavigationVisible: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        // 상태바 아래 콘텐츠가 피그마처럼 62 지점에서 시작하도록 상태바가 낮은 만큼 전체를 내린다.
        modifier = Modifier.padding(top = figmaStatusBarExtraPadding()),
        containerColor = containerColor,
        topBar = {
            fixedTopContent?.invoke()
        },
        bottomBar = {
            Column {
                fixedBottomContent?.invoke()

                if (isBottomNavigationVisible) {
                    HopesBottomNavigation(
                        selectedDestination = selectedDestination,
                        onNavigate = onNavigate,
                    )
                }
            }
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}
