package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
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
