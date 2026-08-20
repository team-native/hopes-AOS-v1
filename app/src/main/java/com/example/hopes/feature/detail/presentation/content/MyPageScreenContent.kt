package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.detail.presentation.MyPageScreenEvent
import com.example.hopes.feature.detail.presentation.MyPageUiState
import com.example.hopes.feature.detail.presentation.component.FigmaDetailPrimaryButton
import com.example.hopes.feature.detail.presentation.component.FigmaDetailPrimaryButtonShadow
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageAccountCard
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageHeader
import com.example.hopes.feature.detail.presentation.component.FigmaMyPageProfileCard
import com.example.hopes.navigation.HopesDestination

/** 피그마 10 마이페이지의 계정·프로필 편집 영역을 구성한다. */
@Composable
fun MyPageScreenContent(
    uiState: MyPageUiState,
    onEvent: (MyPageScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        val density = LocalDensity.current
        val isImeVisible = WindowInsets.ime.getBottom(density) > 0
        val scrollState = rememberScrollState()

        // 키보드가 올라오면 맨 아래로 애니메이션 스크롤해 가려지는 입력창·저장 버튼을 보여준다.
        LaunchedEffect(isImeVisible) {
            if (isImeVisible) {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(bottom = 24.dp)
        ) {
            FigmaMyPageHeader(
                onAppSettingsClick = { onEvent(MyPageScreenEvent.AppSettingsClicked) },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
                FigmaMyPageAccountCard(email = uiState.email, major = uiState.major)

                Spacer(modifier = Modifier.height(28.dp))

                FigmaMyPageProfileCard(
                    profileName = uiState.profileName,
                    profileIntroduction = uiState.profileIntroduction,
                    onProfileNameChange = { onEvent(MyPageScreenEvent.ProfileNameChanged(it)) },
                    onProfileIntroductionChange = {
                        onEvent(MyPageScreenEvent.ProfileIntroductionChanged(it))
                    },
                )

                Spacer(modifier = Modifier.height(18.dp))

                FigmaDetailPrimaryButton(
                    text = when {
                        uiState.isProfileLoading -> stringResource(R.string.profile_loading)
                        uiState.isProfileLoadFailed -> stringResource(R.string.profile_load_error)
                        uiState.isProfileSaving -> stringResource(R.string.profile_saving)
                        uiState.isProfileSaved -> stringResource(R.string.saved)
                        uiState.isProfileSaveFailed -> stringResource(R.string.profile_save_error)
                        else -> stringResource(R.string.save)
                    },
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .width(96.dp)
                        .height(44.dp),
                    onClick = {
                        if (uiState.isProfileLoadFailed) {
                            onEvent(MyPageScreenEvent.ProfileRetryClicked)
                        } else {
                            onEvent(MyPageScreenEvent.ProfileSaveClicked)
                        }
                    },
                    shadowStyle = FigmaDetailPrimaryButtonShadow.Raised,
                )
            }
        }
    }
}
