package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
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
    ) {
        FigmaMyPageHeader(
            onAppSettingsClick = { onEvent(DetailScreenEvent.AppSettingsClicked) },
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp, top = 194.dp)
                .imePadding(),
        ) {
            FigmaMyPageAccountCard()

            Spacer(modifier = Modifier.height(28.dp))

            FigmaMyPageProfileCard(
                profileName = uiState.profileName,
                profileIntroduction = uiState.profileIntroduction,
                onProfileNameChange = { onEvent(DetailScreenEvent.ProfileNameChanged(it)) },
                onProfileIntroductionChange = {
                    onEvent(DetailScreenEvent.ProfileIntroductionChanged(it))
                },
            )

            Spacer(modifier = Modifier.height(18.dp))

            FigmaDetailPrimaryButton(
                text = if (uiState.isProfileSaved) {
                    stringResource(R.string.saved)
                } else {
                    stringResource(R.string.save)
                },
                modifier = Modifier
                    .padding(start = 6.dp)
                    .width(96.dp)
                    .height(44.dp),
                onClick = { onEvent(DetailScreenEvent.ProfileSaveClicked) },
                shadowStyle = FigmaDetailPrimaryButtonShadow.Raised,
            )
        }
    }
}
