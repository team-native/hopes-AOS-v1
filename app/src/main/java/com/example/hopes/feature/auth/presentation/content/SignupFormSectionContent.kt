package com.example.hopes.feature.auth.presentation.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.feature.auth.presentation.SignupValidationUiState
import com.example.hopes.feature.auth.presentation.component.AuthSignupErrorBanner
import com.example.hopes.feature.auth.presentation.component.AuthVerificationCodeField
import com.example.hopes.feature.auth.presentation.component.FigmaSignupField
import com.example.hopes.feature.auth.presentation.component.FigmaSignupSelectionField
import com.example.hopes.feature.auth.presentation.component.SignupFormFieldGroup

/** 회원가입 카드의 입력 행을 세로로 배치한다. */
@Composable
fun SignupFormSectionContent(
    modifier: Modifier,
    emailText: String,
    passwordText: String,
    nameText: String,
    departmentText: String,
    generationText: String,
    verificationCodeText: String,
    signupValidation: SignupValidationUiState,
    isSending: Boolean,
    errorMessage: String?,
    emailHint: String,
    nameHint: String,
    departmentHint: String,
    generationHint: String,
    isSignupEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onDepartmentClick: () -> Unit,
    onGenerationClick: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onSendVerificationCodeClick: () -> Unit,
    onSignupClick: () -> Unit,
) {
    val emailErrorMessage = signupValidation.emailError
        ?.takeIf { signupValidation.isEmailTouched }
        ?.let { stringResource(R.string.signup_error_email) }
    val nameErrorMessage = signupValidation.nameError
        ?.takeIf { signupValidation.isNameTouched }
        ?.let { stringResource(R.string.signup_error_name) }
    val generationErrorMessage = signupValidation.generationError
        ?.takeIf { signupValidation.isGenerationTouched }
        ?.let { stringResource(R.string.signup_error_generation) }
    val passwordErrorMessage = signupValidation.passwordError
        ?.takeIf { signupValidation.isPasswordTouched }
        ?.let { stringResource(R.string.signup_error_password) }
    val verificationCodeErrorMessage = signupValidation.verificationCodeError
        ?.takeIf { signupValidation.isVerificationCodeTouched }
        ?.let { stringResource(R.string.signup_error_verification_code) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .figmaRaisedShadow(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(top = 29.dp, bottom = 27.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (errorMessage != null) {
                AuthSignupErrorBanner()
            }

            SignupFormFieldGroup(labelRes = R.string.email, errorMessage = emailErrorMessage) {
                FigmaSignupField(
                    hint = emailHint,
                    value = emailText,
                    onValueChange = onEmailChange,
                    isError = emailErrorMessage != null,
                )
            }

            SignupFormFieldGroup(
                labelRes = R.string.verification_code,
                errorMessage = verificationCodeErrorMessage,
            ) {
                AuthVerificationCodeField(
                    value = verificationCodeText,
                    isSending = isSending,
                    onValueChange = onVerificationCodeChange,
                    onSendClick = onSendVerificationCodeClick,
                    modifier = Modifier.padding(start = 17.dp, end = 18.dp),
                )
            }

            SignupFormFieldGroup(labelRes = R.string.name, errorMessage = nameErrorMessage) {
                FigmaSignupField(
                    hint = nameHint,
                    value = nameText,
                    onValueChange = onNameChange,
                    isError = nameErrorMessage != null,
                )
            }

            SignupFormFieldGroup(labelRes = R.string.department) {
                FigmaSignupSelectionField(
                    selectedValue = departmentText,
                    placeholder = departmentHint,
                    onClick = onDepartmentClick,
                )
            }

            SignupFormFieldGroup(labelRes = R.string.generation, errorMessage = generationErrorMessage) {
                FigmaSignupSelectionField(
                    selectedValue = generationText,
                    placeholder = generationHint,
                    isError = generationErrorMessage != null,
                    onClick = onGenerationClick,
                )
            }

            SignupFormFieldGroup(labelRes = R.string.password, errorMessage = passwordErrorMessage) {
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
