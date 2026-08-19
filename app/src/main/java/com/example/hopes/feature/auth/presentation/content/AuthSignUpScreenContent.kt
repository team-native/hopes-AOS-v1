package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    // 키보드가 열리면 imePadding이 하단 여백을 확보하고, verticalScroll이 포커스된 필드가
    // 보이는 영역까지 화면 전체를 자연스럽게 스크롤한다. 매직넘버로 화면 전체를 강제로
    // 밀어 올리던 기존 방식(카드가 찌그러지는 원인)을 대체한다.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
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
