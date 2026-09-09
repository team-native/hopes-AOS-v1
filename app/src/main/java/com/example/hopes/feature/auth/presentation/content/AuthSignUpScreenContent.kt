package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.feature.auth.presentation.SignupValidationUiState
import com.example.hopes.feature.auth.presentation.component.AuthSignUpFooterLink
import com.example.hopes.feature.auth.presentation.component.AuthSignUpHeroTitle
import com.example.hopes.feature.auth.presentation.component.SignupActionButton

/** 피그마 03 회원가입 화면 콘텐츠다. 헤더부터 로그인 유도 문구까지 세로로 배치한다. */
@Composable
fun AuthSignUpScreenContent(
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCodeText: String,
    signupValidation: SignupValidationUiState,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    onActionClick: () -> Unit,
    onFooterClick: () -> Unit,
) {
    val isSignupEnabled = !isLoading
    val signupEmailHint = stringResource(R.string.signup_email_hint)
    val signupNameHint = stringResource(R.string.signup_name_hint)
    val signupDepartmentHint = stringResource(R.string.signup_department_hint)
    val signupGenerationHint = stringResource(R.string.signup_generation_hint)
    val density = LocalDensity.current
    val isImeVisible = WindowInsets.ime.getBottom(density) > 0

    // 화면 자체는 스크롤하지 않고, 카드 영역만 weight로 남은 공간을 차지해 내부에서
    // 스크롤된다. 헤더·타이틀·카드가 항상 한 화면에 함께 보이도록 유지한다. 키보드가
    // 열리면 imePadding이 하단 여백을 확보해 카드가 자연스럽게 줄어들고, 카드 내부
    // 스크롤로 모든 입력 필드에 접근할 수 있다.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    ) {
        FigmaBrandHeader(
            modifier = Modifier.padding(start = 32.dp, top = 25.dp),
            isOnBlueBackground = true,
        )

        Spacer(modifier = Modifier.height(87.dp))

        AuthSignUpHeroTitle(modifier = Modifier.padding(start = 32.dp))

        Spacer(modifier = Modifier.height(52.dp))

        SignupFormSectionContent(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            emailText = emailText,
            passwordText = passwordText,
            nameText = nameText,
            departmentText = departmentText,
            generationText = generationText,
            verificationCodeText = verificationCodeText,
            signupValidation = signupValidation,
            isSending = isLoading,
            errorMessage = errorMessage,
            emailHint = signupEmailHint,
            nameHint = signupNameHint,
            departmentHint = signupDepartmentHint,
            generationHint = signupGenerationHint,
            isSignupEnabled = isSignupEnabled,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onNameChange = onNameChange,
            onDepartmentClick = onDepartmentClick,
            onGenerationClick = onGenerationClick,
            onVerificationCodeChange = onVerificationCodeChange,
            onSendVerificationCodeClick = onSendVerificationCodeClick,
            onSignupClick = onActionClick,
        )

        // 키보드가 열려 있는 동안은 회원가입 버튼과 로그인 유도 링크를 숨겨, 좁아진
        // 화면에서도 카드가 더 많은 공간을 차지하도록 한다.
        if (!isImeVisible) {
            Spacer(modifier = Modifier.height(42.dp))

            SignupActionButton(
                isEnabled = isSignupEnabled,
                onClick = onActionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.height(10.dp))

            AuthSignUpFooterLink(onClick = onFooterClick)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
