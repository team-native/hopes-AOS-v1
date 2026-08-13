package com.example.hopes.core.designsystem.component

import androidx.annotation.DrawableRes
import com.example.hopes.R
import com.example.hopes.navigation.HopesDestination

/** 바텀바 목적지와 제공된 아이콘 리소스를 일관되게 연결한다. */
@DrawableRes
internal fun HopesDestination.bottomNavigationIconResourceId(): Int = when (this) {
    HopesDestination.Home -> R.drawable.ic_bottom_navigation_home
    HopesDestination.Chat -> R.drawable.ic_bottom_navigation_chat
    HopesDestination.History -> R.drawable.ic_bottom_navigation_history
    HopesDestination.Settings -> R.drawable.ic_bottom_navigation_profile
    else -> R.drawable.ic_bottom_navigation_home
}
