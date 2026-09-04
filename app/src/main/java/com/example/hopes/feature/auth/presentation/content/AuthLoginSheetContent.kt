package com.example.hopes.feature.auth.presentation.content

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/** 피그마 02 로그인 화면을 구성한다. 시트는 드래그로 여닫을 수 있고, 아래로 스와이프하면 닫힌다. */
@Composable
fun AuthLoginSheetContent(
    emailText: String,
    passwordText: String,
    loginErrorMessage: String?,
    loginStatusMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onDismissLogin: () -> Unit,
    onForgotPasswordClick: () -> Unit,
) {
    val animationScope = rememberCoroutineScope()
    val loginDensity = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 시트 위치는 실제 기기의 사용 가능 높이(maxHeight)만 기준으로 계산한다. 피그마
        // 874dp 프레임과의 차이를 보정하던 기존 방식은 기기별 차이를 흡수하기 위한 것이었는데,
        // maxHeight를 직접 쓰면 그 보정 자체가 필요 없어진다.
        val expandedTopOffsetPx = with(loginDensity) {
            (maxHeight - loginSheetExpandedHeight).toPx()
        }
        val dismissedTopOffsetPx = with(loginDensity) {
            (maxHeight - loginSheetPeekHeight).toPx()
        }
        // 임계값을 닫힘 위치 쪽으로 옮겨, 살짝만 내려도 바로 닫히던 것을 더 많이 내려야
        // 닫히도록 한다. 조금 끌었다가 놓았을 때 열림 위치로 자연스럽게 되돌아간다.
        val dismissThresholdPx = with(loginDensity) {
            (maxHeight - loginSheetDismissThresholdHeight).toPx()
        }
        var sheetTopOffsetPx by remember(maxHeight, loginDensity) {
            mutableFloatStateOf(expandedTopOffsetPx)
        }
        var sheetSettleJob by remember { mutableStateOf<Job?>(null) }

        /** 현재 위치를 기준으로 시트를 열림 또는 닫힘 위치까지 한 번만 이동시킨다. */
        fun settleLoginSheet() {
            sheetSettleJob?.cancel()
            val shouldDismiss = sheetTopOffsetPx > dismissThresholdPx
            val targetOffsetPx = if (shouldDismiss) {
                dismissedTopOffsetPx
            } else {
                expandedTopOffsetPx
            }

            sheetSettleJob = animationScope.launch {
                animate(
                    initialValue = sheetTopOffsetPx,
                    targetValue = targetOffsetPx,
                    animationSpec = tween(LOGIN_SHEET_SETTLE_DURATION_MILLIS),
                ) { animatedOffsetPx, _ ->
                    sheetTopOffsetPx = animatedOffsetPx
                }

                if (shouldDismiss) {
                    onDismissLogin()
                }
            }
        }

        // 시트 높이는 펼침 상태의 가시 높이로 고정하고, 드래그 중에는 draw 단계의 translation만
        // 변경한다. 시트 하단은 화면 밖으로 이동하므로 기존처럼 화면 하단을 계속 덮는다.
        // 드래그 제스처 자체는 시트 전체가 아니라 AuthLoginFormContent의 핸들에만 붙인다 —
        // 시트 전체에 붙이면 필드 목록의 verticalScroll과 제스처가 경합해 드래그로 시트를
        // 내릴 수 없게 되기 때문이다.
        FigmaAuthSheet(
            modifier = Modifier
                .graphicsLayer { translationY = sheetTopOffsetPx }
                .fillMaxWidth()
                .height(loginSheetExpandedHeight),
            isPeekSheet = false,
        ) {
            AuthLoginFormContent(
                emailText = emailText,
                passwordText = passwordText,
                errorMessage = loginErrorMessage,
                statusMessage = loginStatusMessage,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onNavigateSignup = onNavigateSignup,
                onForgotPasswordClick = onForgotPasswordClick,
                onHandleDragStart = {
                    sheetSettleJob?.cancel()
                    sheetSettleJob = null
                },
                onHandleDrag = { deltaPx ->
                    sheetTopOffsetPx = (sheetTopOffsetPx + deltaPx)
                        .coerceIn(expandedTopOffsetPx, dismissedTopOffsetPx)
                },
                onHandleDragEnd = ::settleLoginSheet,
                onHandleDragCancel = ::settleLoginSheet,
            )
        }
    }
}

private val loginSheetExpandedHeight = 502.dp
private val loginSheetPeekHeight = 190.dp
private val loginSheetDismissThresholdHeight = 260.dp
private const val LOGIN_SHEET_SETTLE_DURATION_MILLIS = 180
