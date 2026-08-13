package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.FigmaViewportMetrics
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaDetailPrimaryButton
import com.example.hopes.feature.detail.presentation.component.FigmaDetailPrimaryButtonShadow
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageAccountCard
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageHeader
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageProfileCard
import com.example.hopes.navigation.HopesDestination

/** 피그마 10 마이페이지의 계정·프로필 편집 영역을 구성한다. */
@Composable
fun MyPageScreenContent(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
        imeOverlay = { viewportMetrics ->
            FigmaImeMyPageProfileEditor(
                viewportMetrics = viewportMetrics,
                uiState = uiState,
                onEvent = onEvent,
            )
        },
    ) {
        FigmaMyPageHeader(
            onAppSettingsClick = { onEvent(DetailScreenEvent.AppSettingsClicked) },
        )
        FigmaMyPageAccountCard()
    }
}

/** 키보드 표시 여부와 관계없이 동일한 프로필 입력 필드를 키보드 위로 이동한다. */
@Composable
private fun BoxScope.FigmaImeMyPageProfileEditor(
    viewportMetrics: FigmaViewportMetrics,
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .imePadding()
            .width((402f * viewportMetrics.scale).dp)
            .height((388f * viewportMetrics.scale).dp),
    ) {
        Box(
            modifier = Modifier
                .width(402.dp)
                .height(388.dp)
                .graphicsLayer(
                    scaleX = viewportMetrics.scale,
                    scaleY = viewportMetrics.scale,
                    translationY = -136f * viewportMetrics.scale,
                    transformOrigin = TransformOrigin(0f, 1f),
                ),
        ) {
            FigmaMyPageProfileCard(
                profileName = uiState.profileName,
                profileIntroduction = uiState.profileIntroduction,
                onProfileNameChange = { onEvent(DetailScreenEvent.ProfileNameChanged(it)) },
                onProfileIntroductionChange = {
                    onEvent(DetailScreenEvent.ProfileIntroductionChanged(it))
                },
                modifier = Modifier.padding(start = 12.dp),
            )
            FigmaDetailPrimaryButton(
                text = if (uiState.isProfileSaved) {
                    stringResource(R.string.saved)
                } else {
                    stringResource(R.string.save)
                },
                modifier = Modifier
                    .padding(start = 18.dp, top = 344.dp)
                    .width(96.dp)
                    .height(44.dp),
                onClick = { onEvent(DetailScreenEvent.ProfileSaveClicked) },
                shadowStyle = FigmaDetailPrimaryButtonShadow.Raised,
            )
        }
    }
}
