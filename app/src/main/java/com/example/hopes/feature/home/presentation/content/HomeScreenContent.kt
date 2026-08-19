package com.example.hopes.feature.home.presentation.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.core.designsystem.component.FigmaBrandLogoShadow
import com.example.hopes.feature.home.presentation.component.FigmaHomeTipCard
import com.example.hopes.feature.home.presentation.component.HomeStartChatButton
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
        // edge-to-edge 상태 바 영역도 홈의 파란 배경으로 채운다.
        scaffoldContainerColor = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.padding(start = 32.dp, top = AppSpacing.SystemBarToContent),
        ) {
            FigmaBrandHeader(
                isOnBlueBackground = true,
                logoShadow = FigmaBrandLogoShadow.Subtle,
            )

            // 브랜드 헤더와 온보딩 제목 사이에 요청된 20dp를 추가한다.
            Spacer(modifier = Modifier.height(54.dp))

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

            Spacer(modifier = Modifier.height(85.dp))

            HomeStartChatButton(onClick = onStartChatClick)
        }
    }
}
