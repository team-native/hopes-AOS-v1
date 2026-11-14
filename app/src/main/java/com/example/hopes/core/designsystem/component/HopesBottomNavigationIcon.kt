package com.example.hopes.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hopes.navigation.HopesDestination

/** 하단 탭 목적지마다 피그마 v2의 외곽선 아이콘을 연결한다. */
internal fun HopesDestination.bottomNavigationIcon(): ImageVector =
    when (this) {
        HopesDestination.Home -> Icons.Outlined.Home
        is HopesDestination.Chat -> Icons.AutoMirrored.Outlined.Chat
        HopesDestination.Community -> Icons.Outlined.People
        HopesDestination.History -> Icons.Outlined.AccessTime
        HopesDestination.Settings -> Icons.Outlined.Person
        else -> Icons.Outlined.Home
    }
