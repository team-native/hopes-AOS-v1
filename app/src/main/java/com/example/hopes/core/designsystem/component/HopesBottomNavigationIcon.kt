package com.example.hopes.core.designsystem.component

import androidx.annotation.DrawableRes
import com.example.hopes.navigation.HopesDestination
import com.teamnative.hopes.R

/** 하단 탭 목적지마다 피그마 v2의 외곽선 아이콘(res/drawable/ic_nav_*)을 연결한다. */
@DrawableRes
internal fun HopesDestination.bottomNavigationIcon(): Int =
    when (this) {
        HopesDestination.Home -> R.drawable.ic_nav_home
        is HopesDestination.Chat -> R.drawable.ic_nav_chat
        HopesDestination.Community -> R.drawable.ic_nav_community
        HopesDestination.History -> R.drawable.ic_nav_history
        HopesDestination.Settings -> R.drawable.ic_nav_mypage
        else -> R.drawable.ic_nav_home
    }
