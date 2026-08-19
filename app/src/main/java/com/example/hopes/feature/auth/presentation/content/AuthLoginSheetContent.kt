package com.example.hopes.feature.auth.presentation.content

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.auth.presentation.component.AuthBackdropScrim
import com.example.hopes.feature.auth.presentation.component.AuthHeroCopy
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthLogoShadowStyle
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/** 피그마 02 로그인 화면을 구성한다. 시트는 드래그로 여닫을 수 있고, 아래로 스와이프하면 닫힌다. */
@Composable
fun AuthLoginSheetContent(
    emailText: String,
    passwordText: String,
    loginErrorMessage: String?,
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
        val expandedTopOffset = maxHeight.value - 502f
        val dismissedTopOffset = maxHeight.value - 190f
        // 임계값을 닫힘 위치 쪽으로 옮겨, 살짝만 내려도 바로 닫히던 것을 더 많이 내려야
        // 닫히도록 한다. 조금 끌었다가 놓았을 때 열림 위치로 자연스럽게 되돌아간다.
        val dismissThreshold = maxHeight.value - 260f
        val sheetTopOffset = remember(maxHeight) { Animatable(expandedTopOffset) }

        Box(modifier = Modifier.blur(8.dp)) {
            Column(modifier = Modifier.padding(start = 24.dp, top = 25.dp)) {
                FigmaAuthBrandHeader(logoShadowStyle = FigmaAuthLogoShadowStyle.Login)

                Spacer(modifier = Modifier.height(74.dp))

                AuthHeroCopy()
            }
        }

        AuthBackdropScrim()

        // 시트 위치는 드래그로 매 프레임 바뀌는 값이라, layout을 다시 계산하는 offset 대신
        // draw 단계에서만 이동시키는 graphicsLayer translationY를 쓴다. padding과 달리 음수
        // 값에서도 예외를 던지지 않아, 키보드가 열려 있는 상태에서도 안전하다.
        // 드래그 제스처 자체는 시트 전체가 아니라 AuthLoginFormContent의 핸들에만 붙인다 —
        // 시트 전체에 붙이면 필드 목록의 verticalScroll과 제스처가 경합해 드래그로 시트를
        // 내릴 수 없게 되기 때문이다.
        // 높이는 fillMaxHeight 대신 "화면 하단까지 남은 만큼"만 정확히 준다. translationY로
        // 이미 아래로 밀린 시트에 fillMaxHeight를 주면 화면 밖까지 오버사이즈되어, 내부
        // imePadding·verticalScroll이 실제보다 훨씬 넓은 뷰포트가 있다고 착각해 키보드 위로
        // 충분히 스크롤하지 못하는 원인이 된다.
        // height()를 컴포저블 본문에서 직접 계산하면 sheetTopOffset이 바뀔 때마다(드래그 중
        // 매 프레임) 리컴포지션+리레이아웃이 통째로 일어난다. Modifier.layout으로 값을 읽는
        // 시점을 레이아웃 단계로 미루면 리컴포지션 없이 리레이아웃만 일어나 드래그가 가벼워진다.
        FigmaAuthSheet(
            modifier = Modifier
                .graphicsLayer { translationY = sheetTopOffset.value.dp.toPx() }
                .fillMaxWidth()
                .layout { measurable, constraints ->
                    val heightPx = (maxHeight.toPx() - sheetTopOffset.value.dp.toPx())
                        .coerceAtLeast(0f)
                        .roundToInt()
                    val placeable = measurable.measure(
                        constraints.copy(minHeight = heightPx, maxHeight = heightPx),
                    )
                    layout(placeable.width, heightPx) {
                        placeable.placeRelative(0, 0)
                    }
                },
            isPeekSheet = false,
        ) {
            AuthLoginFormContent(
                emailText = emailText,
                passwordText = passwordText,
                errorMessage = loginErrorMessage,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onLoginClick = onLoginClick,
                onNavigateSignup = onNavigateSignup,
                onForgotPasswordClick = onForgotPasswordClick,
                onHandleDrag = { deltaPx ->
                    animationScope.launch {
                        sheetTopOffset.snapTo(
                            (sheetTopOffset.value + with(loginDensity) { deltaPx.toDp().value })
                                .coerceIn(expandedTopOffset, dismissedTopOffset),
                        )
                    }
                },
                onHandleDragEnd = {
                    animationScope.launch {
                        if (sheetTopOffset.value > dismissThreshold) {
                            sheetTopOffset.animateTo(dismissedTopOffset, tween(180))
                            onDismissLogin()
                        } else {
                            sheetTopOffset.animateTo(expandedTopOffset, tween(180))
                        }
                    }
                },
            )
        }
    }
}
