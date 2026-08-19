package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.hopes.navigation.HopesDestination

/** 뒤로가기·하단 탭 등 공용 Scaffold와 배경을 갖춘 앱 화면 프레임이다. */
@Composable
fun FigmaAppFrame(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    fixedTopContent: (@Composable () -> Unit)? = null,
    fixedBottomContent: (@Composable () -> Unit)? = null,
    isBottomNavigationVisible: Boolean = true,
    background: @Composable BoxScope.() -> Unit = {},
    contentBackgroundColor: Color = MaterialTheme.colorScheme.background,
    scaffoldContainerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldContainerColor),
    ) {
        // 시스템바 아래까지 화면 배경을 그려 Scaffold의 inset 영역과 색이 달라지지 않게 한다.
        background()

        HopesScaffold(
            selectedDestination = selectedDestination,
            onNavigate = onNavigate,
            fixedTopContent = fixedTopContent,
            fixedBottomContent = fixedBottomContent,
            isBottomNavigationVisible = isBottomNavigationVisible,
            containerColor = Color.Transparent,
        ) { innerPadding ->
            FigmaPhoneScreen(
                modifier = Modifier.padding(innerPadding),
                background = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(contentBackgroundColor),
                    )
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(contentBackgroundColor),
                ) {
                    content()
                }
            }
        }
    }
}
