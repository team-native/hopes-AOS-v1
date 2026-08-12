package com.example.hopes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.hopes.navigation.HopesNavigation
import com.example.hopes.ui.theme.HopesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isSystemDarkTheme = isSystemInDarkTheme()
            var isDarkThemeEnabled by rememberSaveable {
                mutableStateOf(isSystemDarkTheme)
            }

            HopesTheme(darkTheme = isDarkThemeEnabled) {
                // 로컬 화면 전환만 제공하는 UI 데모의 진입점이다.
                HopesNavigation(
                    isDarkThemeEnabled = isDarkThemeEnabled,
                    onDarkThemeChange = { isDarkThemeEnabled = it },
                )
            }
        }
    }
}
