package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

/** 피그마 온보딩의 안내 문구와 시작 행동을 제공하는 홈 상단 영역이다. */
@Composable
fun HomeHero(onStartChatClick: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Item)) {
        HopesLogo()
        Text(
            text = stringResource(R.string.home_onboarding_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = stringResource(R.string.home_onboarding_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        HopesPrimaryButton(
            text = stringResource(R.string.start_chat),
            onClick = onStartChatClick,
        )
    }
}
