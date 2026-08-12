package com.example.hopes.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.navigation.HopesDestination

/** 최상위 네 화면으로 이동하는 공통 하단 탐색 바다. */
@Composable
fun HopesBottomNavigation(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
) {
    NavigationBar {
        HopesDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon(),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(text = stringResource(destination.labelResourceId()))
                },
            )
        }
    }
}

private fun HopesDestination.icon() = when (this) {
    HopesDestination.Home -> Icons.Outlined.Home
    HopesDestination.Chat -> Icons.Outlined.ChatBubbleOutline
    HopesDestination.History -> Icons.Outlined.History
    HopesDestination.Settings -> Icons.Outlined.Settings
}

private fun HopesDestination.labelResourceId() = when (this) {
    HopesDestination.Home -> R.string.navigation_home
    HopesDestination.Chat -> R.string.navigation_chat
    HopesDestination.History -> R.string.navigation_history
    HopesDestination.Settings -> R.string.navigation_settings
}
