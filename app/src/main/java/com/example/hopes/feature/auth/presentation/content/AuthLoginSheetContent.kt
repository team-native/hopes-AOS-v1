package com.example.hopes.feature.auth.presentation.content

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
    onForgotPasswordClick: () -> Unit,
    isInitiallyExpanded: Boolean,
    onSheetExpansionProgressChanged: (Float) -> Unit,
) {
    val animationScope = rememberCoroutineScope()
    val loginDensity = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        // 시트 위치는 실제 기기의 사용 가능 높이(maxHeight)만 기준으로 계산한다. 피그마
        // 874dp 프레임과의 차이를 보정하던 기존 방식은 기기별 차이를 흡수하기 위한 것이었는데,
        // maxHeight를 직접 쓰면 그 보정 자체가 필요 없어진다.
        //
        // 이 값들은 키보드와 무관한 순수 화면 기하학적 계산이다. 키보드 대응은 아래
        // graphicsLayer 안에서 draw 단계에만 적용한다(이유는 그 블록의 주석 참고) — 여기서
        // WindowInsets.ime를 섞으면 이 Box가 매 리컴포지션마다 값이 바뀌어 드래그 중
        // coerceIn 경계까지 흔들리게 된다.
        //
        // 가로 모드·분할 화면처럼 maxHeight가 작아지는 경우 시트 상단이 화면 밖(상태바 위)으로
        // 밀려나지 않도록 0 미만으로는 내려가지 않게 고정한다.
        val expandedTopOffsetPx = with(loginDensity) {
            (maxHeight - AUTH_LOGIN_SHEET_EXPANDED_HEIGHT).toPx()
        }.coerceAtLeast(0f)
        val dismissedTopOffsetPx = with(loginDensity) {
            (maxHeight - AUTH_LOGIN_SHEET_PEEK_HEIGHT).toPx()
        }
        // 임계값을 닫힘 위치 쪽으로 옮겨, 살짝만 내려도 바로 닫히던 것을 더 많이 내려야
        // 닫히도록 한다. 조금 끌었다가 놓았을 때 열림 위치로 자연스럽게 되돌아간다.
        val dismissThresholdPx = with(loginDensity) {
            (maxHeight - loginSheetDismissThresholdHeight).toPx()
        }
        var sheetTopOffsetPx by remember(maxHeight, loginDensity) {
            mutableFloatStateOf(
                if (isInitiallyExpanded) {
                    expandedTopOffsetPx
                } else {
                    dismissedTopOffsetPx
                },
            )
        }
        var sheetSettleJob by remember { mutableStateOf<Job?>(null) }

        fun updateSheetTopOffset(offsetPx: Float) {
            sheetTopOffsetPx = offsetPx.coerceIn(expandedTopOffsetPx, dismissedTopOffsetPx)
            onSheetExpansionProgressChanged(
                calculateLoginSheetExpansionProgress(
                    sheetTopOffsetPx = sheetTopOffsetPx,
                    expandedTopOffsetPx = expandedTopOffsetPx,
                    dismissedTopOffsetPx = dismissedTopOffsetPx,
                ),
            )
        }

        SideEffect {
            onSheetExpansionProgressChanged(
                calculateLoginSheetExpansionProgress(
                    sheetTopOffsetPx = sheetTopOffsetPx,
                    expandedTopOffsetPx = expandedTopOffsetPx,
                    dismissedTopOffsetPx = dismissedTopOffsetPx,
                ),
            )
        }

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
                    updateSheetTopOffset(animatedOffsetPx)
                }
            }
        }

        // 키보드 대응은 body-level 상태로 읽지 않고 이 block 안에서만 읽는다. graphicsLayer(block)
        // 람다에서의 상태 읽기는 draw 단계에서만 다시 실행되고 리컴포지션을 유발하지 않는다
        // (sheetTopOffsetPx를 여기서 읽는 기존 설계와 같은 원리 — 커밋 194832e, b8d7e65 참고).
        // WindowInsets.ime.getBottom()은 OS가 키보드를 약 250~300ms에 걸쳐 애니메이션하는 동안
        // 매 프레임 중간값을 준다. 이 값을 body-level val로 읽어 LaunchedEffect의 key로 쓰면
        // 그 애니메이션이 끝날 때까지 매 프레임 새 tween이 이전 tween을 취소하며 경합해
        // 체감 지연과 시트가 최종 위치에 닿지 못하는 것처럼 보이는 문제가 있었다. draw
        // 단계에서 직접 읽으면 OS가 이미 부드럽게 보간해주는 값을 경합 없이 그대로 따라가므로
        // 별도 애니메이션이 필요 없다.
        val imeInsets = WindowInsets.ime

        // 시트 높이는 펼침 상태의 가시 높이로 고정하고, 드래그 중에는 draw 단계의 translation만
        // 변경한다. 시트 하단은 화면 밖으로 이동하므로 기존처럼 화면 하단을 계속 덮는다.
        // 드래그 제스처 자체는 시트 전체가 아니라 AuthLoginFormContent의 핸들에만 붙인다 —
        // 시트 전체에 붙이면 필드 목록의 verticalScroll과 제스처가 경합해 드래그로 시트를
        // 내릴 수 없게 되기 때문이다.
        FigmaAuthSheet(
            modifier = Modifier
                .graphicsLayer {
                    val keyboardShiftPx = imeInsets.getBottom(this)
                    translationY = sheetTopOffsetPx - keyboardShiftPx
                }
                .fillMaxWidth()
                .height(AUTH_LOGIN_SHEET_EXPANDED_HEIGHT),
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
                    updateSheetTopOffset(sheetTopOffsetPx + deltaPx)
                },
                onHandleDragEnd = ::settleLoginSheet,
                onHandleDragCancel = ::settleLoginSheet,
            )
        }
    }
}

// 502dp에서 접근성 글자 확대 등을 위해 560dp까지 늘렸었는데, 늘린 폭(58dp)의 80%를 다시
// 줄여 514dp로 조정한다. 시트 하단은 이 값과 무관하게 항상 화면 하단(maxHeight)에 닿도록
// 계산되므로, 이 값을 조정해도 시트가 화면 밖으로 밀려나지 않는다 — AuthLoginFormContent의
// verticalScroll이 내용 잘림의 최종 안전망이다.
internal val AUTH_LOGIN_SHEET_EXPANDED_HEIGHT = 514.dp
// 부제목이 시스템 내비게이션 영역과 겹치지 않고, 입력 필드는 숨겨지도록 실제 기기에서 측정한 접힘 높이다.
internal val AUTH_LOGIN_SHEET_PEEK_HEIGHT = 156.dp
private val loginSheetDismissThresholdHeight = 260.dp
private const val LOGIN_SHEET_SETTLE_DURATION_MILLIS = 180

/** 로그인 시트 위치를 접힘 0에서 펼침 1 사이의 진행률로 변환한다. */
internal fun calculateLoginSheetExpansionProgress(
    sheetTopOffsetPx: Float,
    expandedTopOffsetPx: Float,
    dismissedTopOffsetPx: Float,
): Float {
    val draggableDistancePx = dismissedTopOffsetPx - expandedTopOffsetPx
    if (draggableDistancePx <= 0f) return 1f

    return ((dismissedTopOffsetPx - sheetTopOffsetPx) / draggableDistancePx)
        .coerceIn(0f, 1f)
}
