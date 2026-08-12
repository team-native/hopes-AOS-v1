package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.core.designsystem.component.HopesLightLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 01~04 인증 화면을 로컬 입력 상태와 함께 제공한다. */
@Composable
fun AuthScreen(
    authStep: AuthStep,
    emailText: String,
    passwordText: String,
    nameText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onNavigateLogin: () -> Unit,
    onDismissLogin: () -> Unit,
    onStartChat: () -> Unit,
) {
    when (authStep) {
        AuthStep.Guide -> AuthGuideContent(onNavigateLogin = onNavigateLogin)
        AuthStep.Login -> LoginSheetScreen(
            emailText = emailText,
            passwordText = passwordText,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onLoginClick = onLoginClick,
            onNavigateSignup = onNavigateSignup,
            onDismissLogin = onDismissLogin,
        )
        AuthStep.SignUp -> AuthFormScreen(
            title = stringResource(R.string.signup),
            subtitle = stringResource(R.string.signup_subtitle),
            emailText = emailText,
            passwordText = passwordText,
            nameText = nameText,
            actionText = stringResource(R.string.signup),
            footerText = stringResource(R.string.has_account),
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNameChange = onNameChange,
            onActionClick = onSignupClick,
            onFooterClick = onNavigateLogin,
        )
        AuthStep.Onboarding -> OnboardingContent(onStartChat = onStartChat)
    }
}

@Composable
private fun AuthGuideContent(onNavigateLogin: () -> Unit) {
    val sheetTopOffset = remember { Animatable(684f) }
    val animationScope = rememberCoroutineScope()
    val guideDensity = LocalDensity.current
    val extendedColors = LocalHopesExtendedColors.current

    FigmaPhoneScreen {
        Box(modifier = Modifier.width(402.dp).height(874.dp)) {
            AuthBackground()
            FigmaAuthBrandHeader(modifier = Modifier.offset(x = 32.dp, y = 76.dp))
            AuthHeroCopy()

            if (sheetTopOffset.value > 520f) {
                SwipeHint(extendedColors = extendedColors)
            }

            // 피그마의 안내 화면처럼 처음에는 190dp만 노출하고, 위로 끌수록 시트를 확장한다.
            FigmaAuthSheet(
                modifier = Modifier
                    .offset(y = sheetTopOffset.value.dp)
                    .width(402.dp)
                    .height((874f - sheetTopOffset.value).coerceAtLeast(190f).dp)
                    .pointerInput(guideDensity) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                animationScope.launch {
                                    sheetTopOffset.snapTo(
                                        (sheetTopOffset.value + with(guideDensity) {
                                            dragAmount.y.toDp().value
                                        }).coerceIn(372f, 684f),
                                    )
                                }
                            },
                            onDragEnd = {
                                animationScope.launch {
                                    if (sheetTopOffset.value < 540f) {
                                        sheetTopOffset.animateTo(372f, tween(durationMillis = 180))
                                        onNavigateLogin()
                                    } else {
                                        sheetTopOffset.animateTo(684f, tween(durationMillis = 180))
                                    }
                                }
                            },
                        )
                    },
                shadowElevation = 12.dp,
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                AuthSheetHandle()

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = stringResource(R.string.login),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.login_guide_subtitle),
                    color = extendedColors.authSubtitle,
                    style = TextStyle(fontSize = 12.sp),
                )
            }
        }
    }
}

/** 피그마 02처럼 배경 위에 고정된 로그인 시트를 표시하고 아래 스와이프로 닫는다. */
@Composable
private fun LoginSheetScreen(
    emailText: String,
    passwordText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
    onDismissLogin: () -> Unit,
) {
    val sheetTopOffset = remember { Animatable(372f) }
    val animationScope = rememberCoroutineScope()
    val loginDensity = LocalDensity.current
    val extendedColors = LocalHopesExtendedColors.current

    FigmaPhoneScreen {
        Box(modifier = Modifier.width(402.dp).height(874.dp)) {
            Box(modifier = Modifier.blur(8.dp)) {
                AuthBackground()
                FigmaAuthBrandHeader(modifier = Modifier.offset(x = 32.dp, y = 76.dp))
                AuthHeroCopy()
            }
            Box(
                modifier = Modifier
                    .width(402.dp)
                    .height(397.dp)
                    .background(extendedColors.authBackdropScrim),
            )
            FigmaAuthSheet(
                modifier = Modifier
                    .offset(y = sheetTopOffset.value.dp)
                    .width(402.dp)
                    .height(502.dp)
                    .pointerInput(loginDensity) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                animationScope.launch {
                                    sheetTopOffset.snapTo(
                                        (sheetTopOffset.value + with(loginDensity) {
                                            dragAmount.y.toDp().value
                                        }).coerceIn(372f, 684f),
                                    )
                                }
                            },
                            onDragEnd = {
                                animationScope.launch {
                                    if (sheetTopOffset.value > 520f) {
                                        sheetTopOffset.animateTo(684f, tween(180))
                                        onDismissLogin()
                                    } else {
                                        sheetTopOffset.animateTo(372f, tween(180))
                                    }
                                }
                            },
                        )
                    },
                shadowElevation = 14.dp,
            ) {
                FigmaLoginSheetContent(
                    emailText = emailText,
                    passwordText = passwordText,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onLoginClick = onLoginClick,
                    onNavigateSignup = onNavigateSignup,
                )
            }
        }
    }
}

@Composable
private fun AuthBackground() {
    Image(
        painter = painterResource(R.drawable.login_guide_background),
        contentDescription = null,
        modifier = Modifier
            .width(402.dp)
            .height(874.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun AuthHeroCopy() {
    val extendedColors = LocalHopesExtendedColors.current

    Column(modifier = Modifier.offset(x = 32.dp, y = 286.dp).width(318.dp)) {
        Text(
            text = stringResource(R.string.auth_title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 39.sp,
            ),
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = stringResource(R.string.auth_description),
            color = extendedColors.authDescription,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
            ),
        )
    }
}

@Composable
private fun SwipeHint(extendedColors: com.example.hopes.ui.theme.HopesExtendedColors) {
    Image(
        painter = painterResource(R.drawable.figma_auth_swipe_arrow_one),
        contentDescription = null,
        modifier = Modifier
            .offset(x = 181.dp, y = 581.dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Image(
        painter = painterResource(R.drawable.figma_auth_swipe_arrow_two),
        contentDescription = null,
        modifier = Modifier
            .offset(x = 181.dp, y = 563.dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Text(
        text = "위로 스와이프하기",
        modifier = Modifier
            .offset(y = 621.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        ),
    )
    Text(
        text = stringResource(R.string.auth_swipe),
        modifier = Modifier
            .offset(y = 651.dp)
            .fillMaxWidth(),
        color = extendedColors.authDescription,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 12.sp),
    )
}

@Composable
private fun AuthSheetHandle() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(86.dp)
                .height(5.dp)
                .background(
                    color = LocalHopesExtendedColors.current.authHandle,
                    shape = RoundedCornerShape(3.dp),
                ),
        )
    }
}

@Composable
private fun FigmaLoginSheetContent(
    emailText: String,
    passwordText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateSignup: () -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val accountPrompt = stringResource(R.string.no_account)
    val signupText = stringResource(R.string.signup)
    val accountPromptAnnotated = AnnotatedString.Builder().apply {
        append(accountPrompt.removeSuffix(signupText))
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
        ) {
            append(signupText)
        }
    }.toAnnotatedString()

    Box(modifier = Modifier.height(502.dp).fillMaxWidth()) {
        Box(
            modifier = Modifier
                .offset(x = 126.dp, y = 20.dp)
                .width(86.dp),
        ) {
            AuthSheetHandle()
        }
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier.offset(y = 68.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            modifier = Modifier.offset(y = 108.dp),
            color = extendedColors.authFieldHint,
            style = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
        )
        AuthFieldLabel(labelRes = R.string.email, modifier = Modifier.offset(y = 162.dp))
        FigmaAuthTextField(
            value = emailText,
            onValueChange = onEmailChange,
            labelRes = R.string.email,
            modifier = Modifier.offset(y = 183.dp),
        )
        AuthFieldLabel(labelRes = R.string.password, modifier = Modifier.offset(y = 238.dp))
        FigmaAuthTextField(
            value = passwordText,
            onValueChange = onPasswordChange,
            labelRes = R.string.password,
            isPassword = true,
            modifier = Modifier.offset(y = 261.dp),
        )
        FigmaLoginButton(
            onClick = onLoginClick,
            modifier = Modifier.offset(y = 355.dp),
        )
        Text(
            text = accountPromptAnnotated,
            modifier = Modifier
                .offset(y = 422.dp)
                .fillMaxWidth()
                .clickable(onClick = onNavigateSignup),
            color = extendedColors.authFieldHint,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
        )
    }
}

@Composable
private fun AuthFieldLabel(labelRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(labelRes),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
    )
}

@Composable
private fun FigmaLoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(AppRadius.Button),
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.login),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun AuthFormScreen(
    title: String,
    subtitle: String,
    emailText: String,
    passwordText: String,
    nameText: String?,
    actionText: String,
    footerText: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Column(modifier = Modifier.padding(horizontal = 32.dp, vertical = 52.dp)) {
                HopesLightLogo()
                Spacer(modifier = Modifier.height(28.dp))
                Text(text = title, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.headlineMedium)
                Text(text = subtitle, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
            }
        }
        HopesSurfaceCard(modifier = Modifier.padding(horizontal = 24.dp, vertical = 26.dp)) {
            OutlinedTextField(value = emailText, onValueChange = onEmailChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.email)) }, singleLine = true)
            OutlinedTextField(value = passwordText, onValueChange = onPasswordChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.password)) }, singleLine = true)
            if (nameText != null) {
                OutlinedTextField(value = nameText, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.name)) }, singleLine = true)
                OutlinedTextField(value = "AI", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.department)) }, singleLine = true)
                OutlinedTextField(value = "10기", onValueChange = {}, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.generation)) }, singleLine = true)
            }
            HopesPrimaryButton(text = actionText, onClick = onActionClick)
            TextButton(onClick = onFooterClick, modifier = Modifier.fillMaxWidth()) {
                Text(text = footerText, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun OnboardingContent(onStartChat: () -> Unit) {
    val extendedColors = LocalHopesExtendedColors.current

    FigmaPhoneScreen {
        Box(modifier = Modifier.width(402.dp).height(874.dp)) {
            AuthBackground()
            FigmaAuthBrandHeader(modifier = Modifier.offset(x = 32.dp, y = 76.dp))
            Text(
                text = stringResource(R.string.onboarding_title),
                modifier = Modifier.offset(x = 32.dp, y = 220.dp).width(318.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 43.sp,
                ),
            )
            Text(
                text = stringResource(R.string.onboarding_description),
                modifier = Modifier.offset(x = 32.dp, y = 330.dp).width(306.dp),
                color = extendedColors.authDescription,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 26.sp,
                ),
            )
            OnboardingTipCard(
                index = 1,
                topText = stringResource(R.string.onboarding_tip_one_top),
                bottomText = stringResource(R.string.onboarding_tip_one_bottom),
                modifier = Modifier.offset(x = 32.dp, y = 450.dp),
            )
            OnboardingTipCard(
                index = 2,
                topText = stringResource(R.string.onboarding_tip_two_top),
                bottomText = stringResource(R.string.onboarding_tip_two_bottom),
                modifier = Modifier.offset(x = 32.dp, y = 543.dp),
            )
            Box(
                modifier = Modifier
                    .offset(x = 32.dp, y = 706.dp)
                    .width(338.dp)
                    .height(46.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(AppRadius.Button),
                    )
                    .clickable(onClick = onStartChat),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.start_chat),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                )
            }
        }
    }
}

@Composable
private fun OnboardingTipCard(
    index: Int,
    topText: String,
    bottomText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(338.dp)
            .height(78.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(18.dp),
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp),
            ),
    ) {
        Box(
            modifier = Modifier
                .offset(x = 14.dp, y = 14.dp)
                .width(24.dp)
                .height(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
            )
        }
        Text(
            text = topText,
            modifier = Modifier.offset(x = 50.dp, y = 13.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.SemiBold),
        )
        Text(
            text = bottomText,
            modifier = Modifier.offset(x = 47.dp, y = 39.dp).width(260.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
            ),
        )
    }
}
