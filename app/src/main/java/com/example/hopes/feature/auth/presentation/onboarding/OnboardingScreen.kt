package com.example.hopes.feature.auth.presentation.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.figmaStatusBarsPadding
import com.example.hopes.feature.auth.presentation.component.AuthPrimaryActionButton
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 04 온보딩이다. 회원가입을 마친 직후에만 한 번 보여 주고, 버튼을 누르면 다음 단계로 넘어간다.
 *
 * @param onStartClick "채팅 시작하기"를 누르거나 뒤로가기를 눌렀을 때 부른다.
 */
@Composable
fun OnboardingScreen(onStartClick: () -> Unit) {
    BackHandler(onBack = onStartClick)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .figmaStatusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = AppSpacing.ScreenHorizontal),
    ) {
        // 피그마 Content: 위 8 + Gap 8 + 요소 간격 16.
        Spacer(modifier = Modifier.height(TOP_GAP))

        OnboardingTitle()

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Text(
            text = stringResource(R.string.onboarding_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Body,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OnboardingTipRow(
            number = "1",
            topText = stringResource(R.string.onboarding_tip_one_top),
            bottomText = stringResource(R.string.onboarding_tip_one_bottom),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        OnboardingTipRow(
            number = "2",
            topText = stringResource(R.string.onboarding_tip_two_top),
            bottomText = stringResource(R.string.onboarding_tip_two_bottom),
        )

        Spacer(modifier = Modifier.weight(1f))

        AuthPrimaryActionButton(
            text = stringResource(R.string.onboarding_start_chat),
            isEnabled = true,
            onClick = onStartClick,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))
    }
}

/** "선배 답변을 / 더 정확하게 받는 방법" 두 줄 제목이다. "선배"만 브랜드 색이다. */
@Composable
private fun OnboardingTitle() {
    val brand = MaterialTheme.colorScheme.primary
    val title =
        buildAnnotatedString {
            withStyle(SpanStyle(color = brand)) { append(stringResource(R.string.auth_hero_highlight_senior)) }
            append(stringResource(R.string.onboarding_title_first_suffix))
            append("\n")
            append(stringResource(R.string.onboarding_title_second))
        }

    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        style = HopesTextStyles.Display,
    )
}

/** 번호 원과 두 줄 문구로 이루어진 팁 한 줄이다. 위아래 여백은 16이다. */
@Composable
private fun OnboardingTipRow(
    number: String,
    topText: String,
    bottomText: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = AppSpacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Item),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier =
                Modifier
                    .size(NUMBER_CIRCLE_SIZE)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number,
                color = MaterialTheme.colorScheme.primary,
                style = HopesTextStyles.Label,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.XSmall),
        ) {
            Text(
                text = topText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
            Text(
                text = bottomText,
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.BodyStrong,
            )
        }
    }
}

private val TOP_GAP = 32.dp
private val NUMBER_CIRCLE_SIZE = 28.dp

@Preview(showBackground = true, widthDp = 402, heightDp = 874)
@Composable
private fun OnboardingScreenPreview() {
    HopesTheme {
        OnboardingScreen(onStartClick = {})
    }
}
