package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.navigation.hopesTabDestinations
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 최상위 다섯 탭(홈·채팅·커뮤니티·기록·마이페이지)으로 이동하는 공통 하단 탐색 바다. 선택된 탭은 연한 파란 배경으로 감싼다. */
@Composable
fun HopesBottomNavigation(
    selectedDestination: HopesDestination,
    onNavigate: (HopesDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .windowInsetsPadding(NavigationBarDefaults.windowInsets),
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = DIVIDER_ALPHA))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(AppSize.BottomNavigation)
                    // 피그마 하단바(402x86)는 위 10, 탭 54, 아래 홈 인디케이터 영역이다.
                    // 홈 인디케이터 자리는 안드로이드 제스처 바 여백(windowInsetsPadding)이 대신한다.
                    .padding(
                        start = NAVIGATION_EDGE_PADDING,
                        end = NAVIGATION_EDGE_PADDING,
                        top = NAVIGATION_EDGE_PADDING,
                    ).selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            hopesTabDestinations.forEach { destination ->
                HopesBottomNavigationItem(
                    destination = destination,
                    isSelected = selectedDestination == destination,
                    onClick = { onNavigate(destination) },
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                )
            }
        }
    }
}

@Composable
private fun HopesBottomNavigationItem(
    destination: HopesDestination,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemShape = RoundedCornerShape(AppRadius.Tab)
    val contentColor =
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Column(
        modifier =
            modifier
                .clip(itemShape)
                .background(
                    color =
                        if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                ).selectable(
                    selected = isSelected,
                    role = Role.Tab,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 바로 아래 라벨이 같은 의미를 전달하므로 아이콘은 장식으로 취급한다.
        Icon(
            painter = painterResource(destination.bottomNavigationIcon()),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppIconSize.BottomNavigation),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = stringResource(destination.labelResourceId()),
            color = contentColor,
            maxLines = 1,
            style = HopesTextStyles.NavigationLabel,
        )
    }
}

private fun HopesDestination.labelResourceId() =
    when (this) {
        HopesDestination.Home -> R.string.navigation_home

        is HopesDestination.Chat -> R.string.navigation_chat

        HopesDestination.Community -> R.string.navigation_community

        HopesDestination.History -> R.string.navigation_history

        // Settings 라우트는 실제로 MyPageRoute(마이페이지)를 보여주므로 라벨도 그에 맞춘다.
        HopesDestination.Settings -> R.string.my_page

        else -> R.string.navigation_home
    }

private const val DIVIDER_ALPHA = 0.5f

@Preview(showBackground = true)
@Composable
private fun HopesBottomNavigationPreview() {
    HopesTheme {
        HopesBottomNavigation(
            selectedDestination = HopesDestination.Home,
            onNavigate = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun HopesBottomNavigationNarrowPreview() {
    HopesTheme {
        HopesBottomNavigation(
            selectedDestination = HopesDestination.Settings,
            onNavigate = {},
        )
    }
}

private val NAVIGATION_EDGE_PADDING = 10.dp
