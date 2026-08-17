package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 최상위 네 화면으로 이동하는 공통 하단 탐색 바다. */
@Composable
fun HopesBottomNavigation(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        HopesDestination.entries
            .filter { destination ->
                destination == HopesDestination.Home ||
                    destination == HopesDestination.Chat ||
                    destination == HopesDestination.History ||
                    destination == HopesDestination.Settings
            }
            .forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(destination.bottomNavigationIconResourceId()),
                        contentDescription = null,
                    )
                },
                label = {
                    Text(text = stringResource(destination.labelResourceId()))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LocalHopesExtendedColors.current.bottomNavigationSelectedIcon,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    // 선택 상태는 아이콘과 라벨 색상으로만 구분하고 배경 pill은 표시하지 않는다.
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

private fun HopesDestination.labelResourceId() = when (this) {
    HopesDestination.Home -> R.string.navigation_home
    HopesDestination.Chat -> R.string.navigation_chat
    HopesDestination.History -> R.string.navigation_history
    HopesDestination.Settings -> R.string.navigation_settings
    else -> R.string.navigation_home
}
