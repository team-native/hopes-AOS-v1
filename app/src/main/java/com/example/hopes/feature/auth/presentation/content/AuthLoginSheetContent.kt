package com.example.hopes.feature.auth.presentation.content

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hopes.feature.auth.presentation.component.AuthBackdropScrim
import com.example.hopes.feature.auth.presentation.component.AuthHeroCopy
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthLogoShadowStyle
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
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
        val dismissThreshold = maxHeight.value - 354f
        val sheetTopOffset = remember(maxHeight) { Animatable(expandedTopOffset) }

        Box(modifier = Modifier.blur(8.dp)) {
            Column(modifier = Modifier.padding(start = 32.dp, top = 25.dp)) {
                FigmaAuthBrandHeader(logoShadowStyle = FigmaAuthLogoShadowStyle.Login)

                Spacer(modifier = Modifier.height(74.dp))

                AuthHeroCopy()
            }
        }

        AuthBackdropScrim()

        // 시트 위치는 드래그로 계속 바뀌는 값이라 padding이 아닌 offset으로 적용한다. offset은
        // padding과 달리 음수 값에서도 예외를 던지지 않아, 키보드가 열려 있는 상태에서도 안전하다.
        FigmaAuthSheet(
            modifier = Modifier
                .offset(y = sheetTopOffset.value.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .pointerInput(loginDensity) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            animationScope.launch {
                                sheetTopOffset.snapTo(
                                    (sheetTopOffset.value + with(loginDensity) {
                                        dragAmount.y.toDp().value
                                    }).coerceIn(expandedTopOffset, dismissedTopOffset),
                                )
                            }
                        },
                        onDragEnd = {
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
            )
        }
    }
}
