package com.example.hopes.feature.community.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 커뮤니티 탭이다. 서버 기능이 아직 없어 하단 탭 자리만 잡고 "준비 중" 안내를 보여준다.
 */
@Composable
fun CommunityRoute(onNavigate: (HopesDestination) -> Unit) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Community,
        onNavigate = onNavigate,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.People,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(AppIconSize.Logo),
            )

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            Text(
                text = stringResource(R.string.community_coming_soon_title),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.SheetTitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            Text(
                text = stringResource(R.string.community_coming_soon_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.Subtitle,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityRoutePreview() {
    HopesTheme {
        CommunityRoute(onNavigate = {})
    }
}
