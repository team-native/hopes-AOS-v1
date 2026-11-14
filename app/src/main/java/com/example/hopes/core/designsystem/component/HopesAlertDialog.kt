package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hopes.core.designsystem.AppBlurRadius
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.overlay.ApplyDialogWindowBackgroundBlur
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme

/**
 * 제목·설명 아래에 두 버튼을 반씩 나눠 둔 확인 다이얼로그다(피그마 32-4 로그아웃 확인, 32-5 로그인 만료).
 *
 * @param isConfirmDestructive true면 확인 버튼을 빨간색으로 그린다(로그아웃, 회원탈퇴처럼 되돌리기 어려운 동작).
 */
@Composable
fun HopesAlertDialog(
    title: String,
    message: String,
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isConfirmDestructive: Boolean = false,
    isDismissibleByOutside: Boolean = true,
) {
    Dialog(onDismissRequest = { if (isDismissibleByOutside) onDismiss() }) {
        ApplyDialogWindowBackgroundBlur(blurRadius = AppBlurRadius.DialogWindowBackdrop)

        HopesAlertDialogContent(
            title = title,
            message = message,
            dismissText = dismissText,
            confirmText = confirmText,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            isConfirmDestructive = isConfirmDestructive,
        )
    }
}

@Composable
private fun HopesAlertDialogContent(
    title: String,
    message: String,
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isConfirmDestructive: Boolean,
) {
    val dialogShape = RoundedCornerShape(AppRadius.Button)

    Column(
        modifier =
            Modifier
                .widthIn(max = DIALOG_MAX_WIDTH)
                .fillMaxWidth()
                .clip(dialogShape)
                .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(AppSpacing.ScreenVertical))

        Text(
            text = title,
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
            text = message,
            modifier = Modifier.padding(horizontal = AppSpacing.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Subtitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
        ) {
            HopesAlertDialogButton(
                text = dismissText,
                color = MaterialTheme.colorScheme.primary,
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.outline,
            )

            HopesAlertDialogButton(
                text = confirmText,
                color =
                    if (isConfirmDestructive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun HopesAlertDialogButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(AppSize.ListItem)
                .clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = color,
            style = HopesTextStyles.FieldStrong,
        )
    }
}

// 피그마 32-4 다이얼로그의 최대 폭이다.
private val DIALOG_MAX_WIDTH = 280.dp

@Preview
@Composable
private fun HopesAlertDialogPreview() {
    HopesTheme {
        HopesAlertDialogContent(
            title = "로그아웃할까요?",
            message = "다시 로그인하면 대화 기록을 그대로 볼 수 있어요.",
            dismissText = "취소",
            confirmText = "로그아웃",
            onDismiss = {},
            onConfirm = {},
            isConfirmDestructive = true,
        )
    }
}
