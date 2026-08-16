package com.example.hopes.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.annotation.StringRes
import kotlinx.coroutines.launch
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppAnimationDuration
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaPhoneScreen
import com.example.hopes.core.designsystem.component.FIGMA_PHONE_HEIGHT
import com.example.hopes.core.designsystem.component.figmaPhoneFrameHeight
import com.example.hopes.core.designsystem.component.figmaPhoneFrameWidth
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.figmaSheetShadow
import com.example.hopes.core.designsystem.component.HopesLightLogo
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.core.designsystem.component.overlay.dialogBackdropBlur
import com.example.hopes.feature.auth.presentation.component.FigmaAuthBrandHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthLogoShadowStyle
import com.example.hopes.feature.auth.presentation.component.FigmaAuthSheet
import com.example.hopes.feature.auth.presentation.component.FigmaSignupHeader
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.feature.auth.presentation.component.FigmaSignupSelectionField
import com.example.hopes.feature.auth.presentation.component.SignupVerificationCodeField
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 01~04 인증 화면을 로컬 입력 상태와 함께 제공한다. */
@Composable
fun AuthScreen(
    authStep: AuthStep,
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCode: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    requestError: AuthRequestError?,
    isSelectionDialogVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit,
    onSendVerificationCode: () -> Unit,
    onNavigateSignup: () -> Unit,
    onNavigateLogin: () -> Unit,
    onDismissLogin: () -> Unit,
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
            emailText = emailText,
            passwordText = passwordText,
            nameText = nameText,
            departmentText = departmentText,
            generationText = generationText,
            verificationCode = verificationCode,
            signupValidation = signupValidation,
            isLoading = isLoading,
            requestError = requestError,
            isSelectionDialogVisible = isSelectionDialogVisible,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNameChange = onNameChange,
            onDepartmentClick = onDepartmentClick,
            onGenerationClick = onGenerationClick,
            onVerificationCodeChange = onVerificationCodeChange,
            onSendVerificationCode = onSendVerificationCode,
            onActionClick = onSignupClick,
            onFooterClick = onNavigateLogin,
        )
    }
}

@Composable
private fun AuthGuideContent(onNavigateLogin: () -> Unit) {
    val sheetTopOffset = remember { Animatable(AUTH_SHEET_COLLAPSED_TOP) }
    val animationScope = rememberCoroutineScope()
    val guideDensity = LocalDensity.current
    val extendedColors = LocalHopesExtendedColors.current
    val guideDragState = rememberDraggableState { dragAmount ->
        // 드래그 delta는 순차적으로 처리해 이동 중 Coroutine이 누적되지 않게 한다.
        sheetTopOffset.snapTo(
            (sheetTopOffset.value + with(guideDensity) {
                dragAmount.toDp().value
            }).coerceIn(
                AUTH_SHEET_EXPANDED_TOP,
                AUTH_SHEET_COLLAPSED_TOP,
            ),
        )
    }

    FigmaPhoneScreen(
        background = {
            AuthBackground(modifier = Modifier.fillMaxSize())
        },
    ) {
        Box(modifier = Modifier.width(figmaPhoneFrameWidth).height(figmaPhoneFrameHeight)) {
            FigmaAuthBrandHeader(modifier = Modifier.padding(start = 32.dp, top = 76.dp))
            AuthHeroCopy()

            if (sheetTopOffset.value > AUTH_SHEET_SWIPE_HINT_THRESHOLD) {
                SwipeHint(extendedColors = extendedColors)
            }

            // 피그마의 안내 화면처럼 처음에는 190dp만 노출하고, 위로 끌수록 시트를 확장한다.
            FigmaAuthSheet(
                modifier = Modifier
                    .padding(top = sheetTopOffset.value.dp)
                    .width(figmaPhoneFrameWidth)
                    .height(
                        (FIGMA_PHONE_HEIGHT - sheetTopOffset.value)
                            .coerceAtLeast(AUTH_SHEET_PEEK_HEIGHT)
                            .dp,
                    )
                    .draggable(
                        state = guideDragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            animationScope.launch {
                                if (sheetTopOffset.value < AUTH_SHEET_OPEN_THRESHOLD) {
                                    sheetTopOffset.animateTo(
                                        AUTH_SHEET_EXPANDED_TOP,
                                        tween(durationMillis = AppAnimationDuration.SheetTransitionMillis),
                                    )
                                    onNavigateLogin()
                                } else {
                                    sheetTopOffset.animateTo(
                                        AUTH_SHEET_COLLAPSED_TOP,
                                        tween(durationMillis = AppAnimationDuration.SheetTransitionMillis),
                                    )
                                }
                            }
                        },
                    )
                isPeekSheet = true,
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
    val sheetTopOffset = remember { Animatable(AUTH_SHEET_EXPANDED_TOP) }
    val animationScope = rememberCoroutineScope()
    val loginDensity = LocalDensity.current
    val extendedColors = LocalHopesExtendedColors.current
    val isImeVisible = WindowInsets.ime.getBottom(loginDensity) > 0
    val keyboardLift = if (isImeVisible) LOGIN_KEYBOARD_LIFT else 0.dp
    val loginDragState = rememberDraggableState { dragAmount ->
        // 드래그 delta는 순차적으로 처리해 이동 중 Coroutine이 누적되지 않게 한다.
        sheetTopOffset.snapTo(
            (sheetTopOffset.value + with(loginDensity) {
                dragAmount.toDp().value
            }).coerceIn(
                AUTH_SHEET_EXPANDED_TOP,
                AUTH_SHEET_COLLAPSED_TOP,
            ),
        )
    }

    FigmaPhoneScreen(
        background = {
            // Figma의 배경은 선명한 그라디언트이고, 전경 브랜드/카피에만 8px 블러가 적용된다.
            AuthBackground(modifier = Modifier.fillMaxSize())
        },
    ) {
        Box(modifier = Modifier.width(figmaPhoneFrameWidth).height(figmaPhoneFrameHeight)) {
            Box(modifier = Modifier.blur(8.dp)) {
                FigmaAuthBrandHeader(
                    modifier = Modifier.padding(start = 32.dp, top = 76.dp),
                    logoShadowStyle = FigmaAuthLogoShadowStyle.Login,
                )
                AuthHeroCopy()
            }
            Box(
                modifier = Modifier
                    .width(figmaPhoneFrameWidth)
                    .height(LOGIN_BACKDROP_HEIGHT)
                    .background(extendedColors.authBackdropScrim),
            )
            FigmaAuthSheet(
                modifier = Modifier
                    .padding(top = sheetTopOffset.value.dp - keyboardLift)
                    .width(figmaPhoneFrameWidth)
                    .height(LOGIN_SHEET_HEIGHT)
                    .draggable(
                        state = loginDragState,
                        orientation = Orientation.Vertical,
                        onDragStopped = {
                            animationScope.launch {
                                if (sheetTopOffset.value > AUTH_SHEET_SWIPE_HINT_THRESHOLD) {
                                    sheetTopOffset.animateTo(
                                        AUTH_SHEET_COLLAPSED_TOP,
                                        tween(AppAnimationDuration.SheetTransitionMillis),
                                    )
                                    onDismissLogin()
                                } else {
                                    sheetTopOffset.animateTo(
                                        AUTH_SHEET_EXPANDED_TOP,
                                        tween(AppAnimationDuration.SheetTransitionMillis),
                                    )
                                }
                            }
                        },
                    )
                isPeekSheet = false,
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
private fun AuthBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.login_guide_background),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun AuthHeroCopy() {
    val extendedColors = LocalHopesExtendedColors.current

    Column(modifier = Modifier.padding(start = 32.dp, top = 286.dp).width(318.dp)) {
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
            .padding(start = 181.dp, top = 581.dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Image(
        painter = painterResource(R.drawable.figma_auth_swipe_arrow_two),
        contentDescription = null,
        modifier = Modifier
            .padding(start = 181.dp, top = 563.dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Text(
        text = stringResource(R.string.auth_swipe_title),
        modifier = Modifier
            .padding(top = 621.dp)
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
            .padding(top = 651.dp)
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
    val isLoginEnabled = emailText.isNotBlank() && passwordText.isNotBlank()
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

    Box(modifier = Modifier.height(LOGIN_SHEET_HEIGHT).fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(start = 126.dp, top = 20.dp)
                .width(86.dp),
        ) {
            AuthSheetHandle()
        }
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier.padding(top = 68.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(R.string.login_subtitle),
            modifier = Modifier.padding(top = 108.dp),
            color = extendedColors.authFieldHint,
            style = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
        )
        AuthFieldLabel(labelRes = R.string.auth_email, modifier = Modifier.padding(top = 162.dp))
        FigmaAuthTextField(
            value = emailText,
            onValueChange = onEmailChange,
            labelRes = R.string.auth_email,
            modifier = Modifier.padding(top = 183.dp),
        )
        AuthFieldLabel(labelRes = R.string.password, modifier = Modifier.padding(top = 238.dp))
        FigmaAuthTextField(
            value = passwordText,
            onValueChange = onPasswordChange,
            labelRes = R.string.password,
            isPassword = true,
            onImeAction = if (isLoginEnabled) onLoginClick else null,
            modifier = Modifier.padding(top = 261.dp),
        )
        FigmaLoginButton(
            isEnabled = isLoginEnabled,
            onClick = onLoginClick,
            modifier = Modifier.padding(top = 355.dp),
        )
        Text(
            text = accountPromptAnnotated,
            modifier = Modifier
                .padding(top = 422.dp)
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
private fun FigmaLoginButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .figmaSheetShadow(RoundedCornerShape(AppRadius.Button))
            .background(
                color = if (isEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                },
                shape = RoundedCornerShape(AppRadius.Button),
            )
            .clickable(enabled = isEnabled, onClick = onClick),
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
    emailText: String,
    passwordText: String,
    nameText: String?,
    departmentText: String,
    generationText: String,
    verificationCode: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    requestError: AuthRequestError?,
    isSelectionDialogVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCode: () -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val signupDensity = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(signupDensity) > 0
    val keyboardLift = if (isImeVisible) SIGN_UP_KEYBOARD_LIFT else 0.dp
    val isSignupEnabled = !isLoading
    val signupEmailHint = stringResource(R.string.signup_email_hint)
    val signupNameHint = stringResource(R.string.signup_name_hint)
    val signupGenerationHint = stringResource(R.string.signup_generation_hint)

    // 상단 헤더를 Scaffold에 고정해 가입 폼 스크롤과 무관하게 화면 전체 폭의 배경을 유지한다.
    Scaffold(
        topBar = ::FigmaSignupHeader,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        FigmaPhoneScreen(modifier = Modifier.padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .width(figmaPhoneFrameWidth)
                    .height(figmaPhoneFrameHeight)
                    .background(MaterialTheme.colorScheme.background)
                    .dialogBackdropBlur(
                        isEnabled = isSelectionDialogVisible,
                        blurRadius = AppBlurRadius.DialogBackground,
                    ),
            ) {
                // 키보드는 Android 시스템 UI로 유지하고, 회원가입 콘텐츠를 위로 이동해 가려지지 않게 한다.
                // 음수 padding은 Compose 예외를 발생시키므로 위치 이동에는 offset을 사용한다.
                Box(modifier = Modifier.offset(y = keyboardLift)) {
                    // 헤더와 가입 버튼 사이를 1f로 사용해 카드의 세로 비율을 유지한다.
                    Column(
                        modifier = Modifier
                            .padding(start = 24.dp, top = 26.dp)
                            .width(354.dp)
                            .height(474.dp),
                    ) {
                        SignupFormCard(
                            modifier = Modifier.weight(1f),
                            emailText = emailText,
                            passwordText = passwordText,
                            nameText = nameText.orEmpty(),
                            departmentText = departmentText,
                            generationText = generationText,
                            verificationCode = verificationCode,
                            signupValidation = signupValidation,
                            isLoading = isLoading,
                            requestError = requestError,
                            emailHint = signupEmailHint,
                            nameHint = signupNameHint,
                            generationHint = signupGenerationHint,
                            isSignupEnabled = isSignupEnabled,
                            onEmailChange = onEmailChange,
                            onPasswordChange = onPasswordChange,
                            onNameChange = onNameChange,
                            onDepartmentClick = onDepartmentClick,
                            onGenerationClick = onGenerationClick,
                            onVerificationCodeChange = onVerificationCodeChange,
                            onSendVerificationCode = onSendVerificationCode,
                            onSignupClick = onActionClick,
                        )

                        Spacer(modifier = Modifier.height(42.dp))

                        SignupActionButton(
                            isEnabled = isSignupEnabled,
                            onClick = onActionClick,
                        )
                    }

                    Text(
                        text = stringResource(R.string.has_account),
                        modifier = Modifier
                            .padding(top = 510.dp)
                            .fillMaxWidth()
                            .clickable(onClick = onFooterClick),
                        color = extendedColors.authFieldHint,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                    )
                }
            }
        }
    }
}

private const val AUTH_SHEET_EXPANDED_TOP = 372f
private const val AUTH_SHEET_COLLAPSED_TOP = 684f
private const val AUTH_SHEET_OPEN_THRESHOLD = 540f
private const val AUTH_SHEET_SWIPE_HINT_THRESHOLD = 520f
private const val AUTH_SHEET_PEEK_HEIGHT = 190f
private val LOGIN_KEYBOARD_LIFT = 126.dp
private val LOGIN_BACKDROP_HEIGHT = 397.dp
private val LOGIN_SHEET_HEIGHT = 502.dp
private val SIGN_UP_KEYBOARD_LIFT = (-120).dp

/** 회원가입 카드의 입력 항목을 지연 목록으로 배치한다. */
@Composable
private fun SignupFormCard(
    modifier: Modifier,
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCode: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    requestError: AuthRequestError?,
    emailHint: String,
    nameHint: String,
    generationHint: String,
    isSignupEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCode: () -> Unit,
    onSignupClick: () -> Unit,
) {
    val emailErrorMessage = signupValidation.emailError
        ?.takeIf { signupValidation.isEmailTouched }
        ?.let { error ->
        stringResource(error.messageRes())
    }
    val nameErrorMessage = signupValidation.nameError
        ?.takeIf { signupValidation.isNameTouched }
        ?.let { error ->
        stringResource(error.messageRes())
    }
    val generationErrorMessage = signupValidation.generationError
        ?.takeIf { signupValidation.isGenerationTouched }
        ?.let { error ->
        stringResource(error.messageRes())
    }
    val passwordErrorMessage = signupValidation.passwordError
        ?.takeIf { signupValidation.isPasswordTouched }
        ?.let { error ->
        stringResource(error.messageRes())
    }
    val verificationCodeErrorMessage = signupValidation.verificationCodeError
        ?.takeIf { signupValidation.isVerificationCodeTouched }
        ?.let { error ->
            stringResource(error.messageRes())
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 29.dp, bottom = 27.dp),
        ) {
            item {
            if (requestError == AuthRequestError.SendVerificationCodeFailed) {
                Text(
                    text = stringResource(R.string.verification_error_send),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
                )
            }

            if (requestError == AuthRequestError.SignUpFailed) {
                Text(
                    text = stringResource(R.string.verification_error_signup),
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
                )
            }
            }

            item {
            SignupFormFieldGroup(
                labelRes = R.string.email,
                errorMessage = emailErrorMessage,
            ) {
                FigmaSignupField(
                    hint = emailHint,
                    value = emailText,
                    onValueChange = onEmailChange,
                    isError = emailErrorMessage != null,
                )
            }
            }
            item {
            SignupFormFieldGroup(
                labelRes = R.string.verification_code,
                errorMessage = verificationCodeErrorMessage,
            ) {
                SignupVerificationCodeField(
                    value = verificationCode,
                    isSending = isLoading,
                    onValueChange = onVerificationCodeChange,
                    onSendClick = onSendVerificationCode,
                )
            }
            }
            item {
            SignupFormFieldGroup(
                labelRes = R.string.name,
                errorMessage = nameErrorMessage,
            ) {
                FigmaSignupField(
                    hint = nameHint,
                    value = nameText,
                    onValueChange = onNameChange,
                    isError = nameErrorMessage != null,
                )
            }
            }
            item {
            SignupFormFieldGroup(
                labelRes = R.string.department,
            ) {
                FigmaSignupSelectionField(
                    selectedValue = departmentText,
                    placeholder = stringResource(R.string.signup_department_hint),
                    onClick = onDepartmentClick,
                )
            }
            }
            item {
            SignupFormFieldGroup(
                labelRes = R.string.generation,
                errorMessage = generationErrorMessage,
            ) {
                FigmaSignupSelectionField(
                    selectedValue = generationText,
                    placeholder = generationHint,
                    isError = generationErrorMessage != null,
                    onClick = onGenerationClick,
                )
            }
            }
            item {
            SignupFormFieldGroup(
                labelRes = R.string.password,
                errorMessage = passwordErrorMessage,
            ) {
                FigmaSignupField(
                    hint = stringResource(R.string.password),
                    value = passwordText,
                    onValueChange = onPasswordChange,
                    isPassword = true,
                    isError = passwordErrorMessage != null,
                    onImeAction = if (isSignupEnabled) onSignupClick else null,
                )
            }
            }
        }
    }
}

/** 한 입력 행에서 라벨과 필드를 남은 세로 공간 안에 정렬한다. */
@Composable
private fun SignupFormFieldGroup(
    labelRes: Int,
    errorMessage: String? = null,
    field: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 66.dp),
    ) {
        Text(
            text = stringResource(labelRes),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Spacer(modifier = Modifier.height(6.dp))

        field()

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(start = 24.dp, top = 4.dp, end = 24.dp),
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
            )
        }
    }
}

/** 회원가입 버튼은 카드의 가변 높이와 분리해 항상 하단 기준을 유지한다. */
@Composable
private fun SignupActionButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .background(
                color = if (isEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                },
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.signup_submit),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

/** 피그마 03 회원가입 카드의 가용 폭을 모두 사용하는 43dp 입력 행이다. */
@Composable
private fun FigmaSignupField(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    onImeAction: (() -> Unit)? = null,
) {
    val extendedColors = LocalHopesExtendedColors.current
    // 카드 좌우 여백을 제외한 나머지 폭을 1f로 배분해 기기 폭 변화에도 필드 비율을 유지한다.
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 17.dp, end = 18.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(43.dp)
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        extendedColors.authFieldBorder
                    },
                    shape = RoundedCornerShape(14.dp),
                ),
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = extendedColors.authFieldHint,
                    style = TextStyle(fontSize = 15.sp),
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterStart),
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                ),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    imeAction = if (isPassword) {
                        androidx.compose.ui.text.input.ImeAction.Done
                    } else {
                        androidx.compose.ui.text.input.ImeAction.Next
                    },
                ),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onDone = { onImeAction?.invoke() },
                ),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            )
        }
    }
}

@StringRes
private fun SignupInputError.messageRes(): Int {
    return when (this) {
        SignupInputError.InvalidSchoolEmail -> R.string.signup_error_email
        SignupInputError.InvalidUsername -> R.string.signup_error_name
        SignupInputError.GenerationRequired -> R.string.signup_error_generation
        SignupInputError.InvalidPassword -> R.string.signup_error_password
        SignupInputError.InvalidVerificationCode -> R.string.signup_error_verification_code
    }
}
