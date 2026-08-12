package com.example.hopes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hopes.navigation.HopesNavigation
import com.example.hopes.ui.theme.HopesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HopesTheme {
                // 로컬 화면 전환만 제공하는 UI 데모의 진입점이다.
                HopesNavigation()
            }
        }
    }
}
