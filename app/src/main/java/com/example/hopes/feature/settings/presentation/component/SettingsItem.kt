package com.example.hopes.feature.settings.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 향후 설정 기능이 연결될 단일 안내 항목이다. */
@Composable
fun SettingsItem(text: String) {
    HopesSurfaceCard {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
