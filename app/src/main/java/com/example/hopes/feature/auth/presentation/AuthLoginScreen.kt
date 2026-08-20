package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.feature.auth.presentation.component.AuthBackground
import com.example.hopes.feature.auth.presentation.content.AuthLoginSheetContent
import kotlin.random.Random

/** 피그마 02 로그인 화면 진입점이다. */
@Composable
fun AuthLoginScreen(
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
    FigmaPhoneScreen(
        applyStatusBarsPadding = true,
        navigationBarColor = MaterialTheme.colorScheme.surface,
        background = {
            // 배경 그라디언트는 색 변화가 완만해 블러만 걸면 눈에 안 띄므로, 옅은 노이즈를
            // 함께 그려서 시트 뒤 배경도 로고/카피(AuthLoginSheetContent)만큼 또렷하게
            // 블러가 보이도록 한다.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(8.dp),
            ) {
                AuthBackground(modifier = Modifier.fillMaxSize())
                Box(modifier = Modifier.fillMaxSize().authBackgroundNoise())
            }
        },
    ) {
        AuthLoginSheetContent(
            emailText = emailText,
            passwordText = passwordText,
            loginErrorMessage = loginErrorMessage,
            loginStatusMessage = loginStatusMessage,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onNavigateSignup = onNavigateSignup,
            onDismissLogin = onDismissLogin,
            onForgotPasswordClick = onForgotPasswordClick,
        )
    }
}

/**
 * 매끈한 그라디언트 위에 아주 옅은 흰 점 노이즈를 흩뿌려, blur()가 실제로 부드럽게 만들
 * 대비를 만들어준다. size가 바뀔 때만 점 위치를 다시 계산해 매 프레임 다시 그리지 않는다.
 */
private fun Modifier.authBackgroundNoise(): Modifier = drawWithCache {
    val random = Random(20260821)
    val dotCount = (size.width * size.height / 4000f).toInt().coerceAtLeast(1)
    val dots = List(dotCount) {
        Triple(
            Offset(random.nextFloat() * size.width, random.nextFloat() * size.height),
            random.nextFloat() * 2.5f + 0.5f,
            random.nextFloat() * 0.12f + 0.04f,
        )
    }

    onDrawBehind {
        dots.forEach { (offset, radius, alpha) ->
            drawCircle(color = Color.White.copy(alpha = alpha), radius = radius, center = offset)
        }
    }
}
