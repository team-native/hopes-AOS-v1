package com.example.hopes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.example.hopes.navigation.HopesNavHost
import com.example.hopes.ui.theme.HopesTheme
import dagger.hilt.android.AndroidEntryPoint

/** Compose Route에서 Hilt ViewModel을 생성할 수 있도록 등록한 Activity 진입점이다. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 시스템바에 별도 색을 입히지 않고 화면 콘텐츠가 뒤까지 그려지도록 edge-to-edge를 활성화한다.
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var isDarkThemeEnabled by rememberSaveable {
                // Figma 기준 기본 상태는 라이트 테마다.
                mutableStateOf(false)
            }

            SideEffect {
                WindowCompat.getInsetsController(
                    window,
                    window.decorView,
                ).apply {
                    isAppearanceLightStatusBars = !isDarkThemeEnabled
                    isAppearanceLightNavigationBars = !isDarkThemeEnabled
                }
            }

            HopesTheme(darkTheme = isDarkThemeEnabled) {
                // 로컬 화면 전환만 제공하는 UI 데모의 진입점이다.
                HopesNavHost(
                    isDarkThemeEnabled = isDarkThemeEnabled,
                    onDarkThemeChange = { isDarkThemeEnabled = it },
                )
            }
        }
    }
}
