package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.component.AuthHeroHeadline
import com.example.hopes.feature.auth.presentation.component.AuthLinkPrompt
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.feature.auth.presentation.component.AuthStartCloudIllustration
import com.example.hopes.feature.auth.presentation.component.AuthStartDescription
import com.example.hopes.feature.auth.presentation.component.AuthStartLogoBadge
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 01 로그인 안내(시작) 화면이다. 시작하기는 회원가입, 하단 링크는 로그인 단계로 이동한다. */
@Composable
fun AuthStartScreen(
    onStartClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        AuthStartCloudIllustration(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = CLOUD_BOTTOM_OFFSET),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
        ) {
            Spacer(modifier = Modifier.weight(HERO_TOP_WEIGHT))

            AuthStartLogoBadge()

            Spacer(modifier = Modifier.height(AppSpacing.Section))

            AuthHeroHeadline()

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            AuthStartDescription()

            Spacer(modifier = Modifier.weight(HERO_BOTTOM_WEIGHT))

            AuthPrimaryActionButton(
                text = stringResource(R.string.auth_start_button),
                isEnabled = true,
                onClick = onStartClick,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            AuthLinkPrompt(
                promptText = stringResource(R.string.auth_has_account_prompt),
                linkText = stringResource(R.string.login),
                onLinkClick = onLoginClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Compact))
        }
    }
}

// 피그마에서 헤드라인 블록은 화면 중앙보다 약간 위, 버튼은 하단에 붙는다.
private const val HERO_TOP_WEIGHT = 1f
private const val HERO_BOTTOM_WEIGHT = 1.3f
private val CLOUD_BOTTOM_OFFSET = 96.dp

@Preview(showBackground = true)
@Composable
private fun AuthStartScreenPreview() {
    HopesTheme {
        AuthStartScreen(
            onStartClick = {},
            onLoginClick = {},
        )
    }
}
