package com.example.hopes.feature.auth.presentation.signup.view.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesInlineErrorMessage
import com.example.hopes.feature.auth.presentation.component.AuthCheckboxRow
import com.example.hopes.feature.auth.presentation.component.AuthFieldLabel
import com.example.hopes.feature.auth.presentation.component.AuthTextInputField
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupHintText
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupSelectionField
import com.example.hopes.feature.auth.presentation.signup.view.component.SignupStepTitle
import com.example.hopes.feature.auth.presentation.signup.view.labelResId
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupScreenEvent
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupUiState
import com.teamnative.hopes.R

/** 3단계 프로필 입력 화면(03-10)이다. 이름·과·기수와 필수 약관 동의를 받는다. */
@Composable
fun SignupProfileStepContent(
    uiState: SignupUiState,
    requestErrorMessage: String?,
    onEvent: (SignupScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val generationErrorMessage =
        if (uiState.isGenerationErrorVisible) {
            stringResource(R.string.signup_generation_required)
        } else {
            null
        }
    val departmentLabel = stringResource(R.string.signup_department_label)
    val generationLabel = stringResource(R.string.signup_generation_label)

    Column(modifier = modifier.fillMaxWidth()) {
        SignupStepTitle(
            text = stringResource(R.string.signup_profile_title),
            highlight = stringResource(R.string.signup_profile_title_highlight),
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        SignupHintText(text = stringResource(R.string.signup_profile_subtitle))

        Spacer(modifier = Modifier.height(AppSpacing.Section))

        AuthFieldLabel(labelRes = R.string.signup_name_label)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        AuthTextInputField(
            value = uiState.name,
            onValueChange = { value -> onEvent(SignupScreenEvent.NameChanged(value)) },
            placeholderRes = R.string.signup_name_placeholder,
            errorMessage = null,
            isEnabled = !uiState.isLoading,
            imeAction = ImeAction.Done,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        AuthFieldLabel(labelRes = R.string.signup_department_label)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        SignupSelectionField(
            value = uiState.department?.let { department -> stringResource(department.labelResId()) },
            placeholder = stringResource(R.string.signup_department_placeholder),
            label = departmentLabel,
            errorMessage = null,
            onClick = { onEvent(SignupScreenEvent.DepartmentFieldClicked) },
        )

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        AuthFieldLabel(labelRes = R.string.signup_generation_label)

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        SignupSelectionField(
            value =
                uiState.generation?.let { generation ->
                    stringResource(R.string.signup_generation_option, generation)
                },
            placeholder = stringResource(R.string.signup_generation_hint),
            label = generationLabel,
            errorMessage = generationErrorMessage,
            onClick = { onEvent(SignupScreenEvent.GenerationFieldClicked) },
        )

        if (generationErrorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesInlineErrorMessage(message = generationErrorMessage)
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        AuthCheckboxRow(
            text = stringResource(R.string.signup_terms_agreement),
            isChecked = uiState.isTermsAgreed,
            onCheckedChange = { isAgreed -> onEvent(SignupScreenEvent.TermsAgreementChanged(isAgreed)) },
        )

        if (requestErrorMessage != null) {
            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesInlineErrorMessage(message = requestErrorMessage)
        }
    }
}
