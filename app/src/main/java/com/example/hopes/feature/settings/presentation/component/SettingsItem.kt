package com.example.hopes.feature.settings.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.HopesSurfaceCard

/** 향후 설정 기능이 연결될 단일 안내 항목이다. */
@Composable
fun SettingsItem(
    text: String,
    description: String,
    onClick: () -> Unit,
) {
    HopesSurfaceCard(modifier = Modifier.clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
                Text(text = description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = onClick) { Text(text = stringResource(R.string.open)) }
        }
    }
}
