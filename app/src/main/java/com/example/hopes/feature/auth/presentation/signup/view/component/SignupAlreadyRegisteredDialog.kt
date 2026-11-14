package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 인증번호 발송 시 이미 가입된 이메일이면 띄우는 안내 다이얼로그다(피그마 03-2). */
@Composable
fun SignupAlreadyRegisteredDialog(onDismiss: () -> Unit) {
    val extendedColors = LocalHopesExtendedColors.current

    Dialog(onDismissRequest = onDismiss) {
        ApplyDialogWindowBackgroundBlur(blurRadius = AppBlurRadius.DialogWindowBackdrop)

        Column(
            modifier =
                Modifier
                    .widthIn(max = DIALOG_MAX_WIDTH)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(AppRadius.Button),
                    ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(DIALOG_TOP_PADDING))

            Text(
                text = stringResource(R.string.signup_already_registered_title),
                modifier =
                    Modifier
                        .padding(horizontal = AppSpacing.Medium)
                        .semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.DialogTitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            Text(
                text = stringResource(R.string.signup_already_registered_message),
                modifier = Modifier.padding(horizontal = AppSpacing.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = HopesTextStyles.Subtitle,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Medium))

            HorizontalDivider(color = extendedColors.authFieldBorder)

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(AppSize.ListItem)
                        .clickable(
                            role = Role.Button,
                            onClick = onDismiss,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.signup_dialog_confirm),
                    color = MaterialTheme.colorScheme.primary,
                    style = HopesTextStyles.FieldStrong,
                )
            }
        }
    }
}

// 피그마 03-2 다이얼로그의 최대 폭과 제목 위 여백이다.
private val DIALOG_MAX_WIDTH = 280.dp
private val DIALOG_TOP_PADDING = 20.dp
