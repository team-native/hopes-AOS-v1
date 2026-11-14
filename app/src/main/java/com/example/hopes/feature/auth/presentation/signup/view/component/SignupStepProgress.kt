package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupStep
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 3칸 진행 바와 "1 / 3 · 이메일 인증" 단계 문구다. 현재 단계까지 칸을 primary 색으로 채운다. */
@Composable
fun SignupStepProgress(
    step: SignupStep,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val totalStepCount = SignupStep.entries.size
    val progressText =
        stringResource(
            R.string.signup_step_progress,
            step.number,
            totalStepCount,
            stringResource(step.labelResId()),
        )

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clearAndSetSemantics { contentDescription = progressText },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small),
        ) {
            SignupStep.entries.forEach { entry ->
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(PROGRESS_BAR_HEIGHT)
                            .background(
                                color =
                                    if (entry.number <= step.number) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        extendedColors.authHandle
                                    },
                                shape = RoundedCornerShape(PROGRESS_BAR_RADIUS),
                            ),
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = progressText,
            color = MaterialTheme.colorScheme.primary,
            style = HopesTextStyles.Overline,
        )
    }
}

// 피그마 진행 바의 두께와 모서리다.
private val PROGRESS_BAR_HEIGHT = 3.dp
private val PROGRESS_BAR_RADIUS = 2.dp

private fun SignupStep.labelResId(): Int =
    when (this) {
        SignupStep.Email -> R.string.signup_step_email
        SignupStep.Password -> R.string.signup_step_password
        SignupStep.Profile -> R.string.signup_step_profile
    }
