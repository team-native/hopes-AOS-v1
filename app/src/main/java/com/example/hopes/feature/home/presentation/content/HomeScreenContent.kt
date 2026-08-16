package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.FigmaBrandLogoShadow
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 04 온보딩 프레임을 로그인 뒤 홈 탭의 첫 화면으로 표시한다. */
@Composable
fun HomeScreenContent(
    onStartChatClick: () -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current
    FigmaAppFrame(
        selectedDestination = HopesDestination.Home,
        onNavigate = onNavigate,
        background = {
            Image(
                painter = painterResource(R.drawable.login_guide_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        },
        contentBackgroundColor = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.padding(start = 32.dp, top = 76.dp),
        ) {
            FigmaBrandHeader(
                isOnBlueBackground = true,
                logoShadow = FigmaBrandLogoShadow.Subtle,
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = stringResource(R.string.onboarding_title),
                modifier = Modifier.width(318.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold, lineHeight = 43.sp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.onboarding_description),
                modifier = Modifier.width(306.dp),
                color = extendedColors.authDescription,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium, lineHeight = 26.sp),
            )

            Spacer(modifier = Modifier.height(68.dp))

            FigmaHomeTipCard(
                index = 1,
                topText = stringResource(R.string.onboarding_tip_one_top),
                bottomText = stringResource(R.string.onboarding_tip_one_bottom),
            )

            Spacer(modifier = Modifier.height(15.dp))

            FigmaHomeTipCard(
                index = 2,
                topText = stringResource(R.string.onboarding_tip_two_top),
                bottomText = stringResource(R.string.onboarding_tip_two_bottom),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .width(338.dp)
                    .height(46.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                    .clickable(onClick = onStartChatClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.start_chat),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

/** 피그마 온보딩의 번호·설명 카드 구성요소다. */
@Composable
private fun FigmaHomeTipCard(
    index: Int,
    topText: String,
    bottomText: String,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = Modifier
            .width(338.dp)
            .height(78.dp)
            .figmaSubtleShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
    ) {
        Box(
            modifier = Modifier
                .padding(start = 14.dp, top = 14.dp)
                .width(24.dp)
                .height(24.dp)
                .background(extendedColors.onboardingStepContainer, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                color = extendedColors.onboardingStepText,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            )
        }
        Text(
            text = topText,
            modifier = Modifier.padding(start = 50.dp, top = 15.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
            ),
        )
        Text(
            text = bottomText,
            modifier = Modifier.padding(start = 47.dp, top = 39.dp).width(260.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp),
        )
    }
}
