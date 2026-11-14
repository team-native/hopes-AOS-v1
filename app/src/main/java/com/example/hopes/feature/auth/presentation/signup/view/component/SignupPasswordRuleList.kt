package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupPasswordRules
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/**
 * 비밀번호 조건 3개(8자 이상, 영문과 숫자, 특수문자)를 표시한다.
 * 충족한 조건은 파란 체크, 오류 상태에서 미충족 조건은 빨간 엑스, 그 외에는 회색 체크로 그린다.
 */
@Composable
fun SignupPasswordRuleList(
    rules: SignupPasswordRules,
    isErrorVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val ruleItems =
        listOf(
            R.string.signup_password_rule_length to rules.hasMinimumLength,
            R.string.signup_password_rule_letter_digit to rules.hasLetterAndDigit,
            R.string.signup_password_rule_special to rules.hasSpecialCharacter,
        )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small),
    ) {
        ruleItems.forEach { (labelResId, isMet) ->
            val label = stringResource(labelResId)
            val color =
                when {
                    isMet -> MaterialTheme.colorScheme.primary
                    isErrorVisible -> MaterialTheme.colorScheme.error
                    else -> LocalHopesExtendedColors.current.authFieldHint
                }

            SignupCheckMessage(
                text = label,
                isChecked = isMet || !isErrorVisible,
                color = color,
                accessibilityText =
                    stringResource(
                        if (isMet) {
                            R.string.signup_password_rule_met
                        } else {
                            R.string.signup_password_rule_unmet
                        },
                        label,
                    ),
            )
        }
    }
}
