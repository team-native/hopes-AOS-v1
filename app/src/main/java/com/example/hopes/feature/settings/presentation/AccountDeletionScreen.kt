package com.example.hopes.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesAlertDialog
import com.example.hopes.core.designsystem.component.HopesFieldLabel
import com.example.hopes.core.designsystem.component.HopesInlineErrorMessage
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.core.designsystem.component.HopesTextField
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 32-6 ~ 32-9 회원탈퇴 화면이다. 삭제될 정보 안내 카드와 비밀번호 확인 카드, 아래 빨간 탈퇴하기 버튼을 둔다.
 * 비밀번호가 맞지 않으면 입력란이 빨갛게 바뀌고 오류 문구가 붙는다(32-8). 버튼을 누르면 확인 다이얼로그가 뜬다(32-9).
 *
 * @param errorMessage null이 아니면 비밀번호 입력란을 오류 상태로 그리고 이 문구를 아래에 붙인다.
 */
@Composable
fun AccountDeletionScreen(
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?,
    isDeleting: Boolean,
    isConfirmDialogVisible: Boolean,
    onSubmitClick: () -> Unit,
    onConfirmDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onBackClick: () -> Unit,
) {
    val isSubmitEnabled = password.isNotBlank() && !isDeleting

    HopesSubScreenFrame(
        title = stringResource(R.string.account_deletion_title),
        subtitle = stringResource(R.string.account_deletion_subtitle),
        onBackClick = onBackClick,
        bottomButton = {
            HopesPrimaryButton(
                text = stringResource(R.string.account_deletion_submit),
                onClick = onSubmitClick,
                isEnabled = isSubmitEnabled,
                isDestructive = true,
            )
        },
    ) {
        AccountDeletionNoticeCard()

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        HopesSurfaceCard {
            HopesFieldLabel(text = stringResource(R.string.account_deletion_password_label))

            Spacer(modifier = Modifier.height(LABEL_FIELD_GAP))

            HopesTextField(
                value = password,
                onValueChange = onPasswordChange,
                placeholder = stringResource(R.string.account_deletion_password_placeholder),
                accessibilityLabel = stringResource(R.string.account_deletion_password_label),
                errorMessage = errorMessage,
                isEnabled = !isDeleting,
                imeAction = ImeAction.Done,
                isPassword = true,
                onImeAction = { if (isSubmitEnabled) onSubmitClick() },
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(LABEL_FIELD_GAP))

                HopesInlineErrorMessage(message = errorMessage)
            }
        }
    }

    if (isConfirmDialogVisible) {
        HopesAlertDialog(
            title = stringResource(R.string.account_deletion_confirm_title),
            message = stringResource(R.string.account_deletion_confirm_message),
            dismissText = stringResource(R.string.account_deletion_cancel),
            confirmText = stringResource(R.string.account_deletion_submit),
            isConfirmDestructive = true,
            onDismiss = onConfirmDismiss,
            onConfirm = onConfirm,
        )
    }
}

/** 탈퇴하면 삭제되는 정보를 나열한 카드다. 마지막 줄은 빨간 글씨다(피그마 Notice Card). */
@Composable
private fun AccountDeletionNoticeCard() {
    HopesSurfaceCard {
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.Compact)) {
            NoticeLine(text = stringResource(R.string.account_deletion_notice_title))
            NoticeLine(text = stringResource(R.string.account_deletion_notice_profile))
            NoticeLine(text = stringResource(R.string.account_deletion_notice_chats))
            NoticeLine(text = stringResource(R.string.account_deletion_notice_contacts))
            NoticeLine(
                text = stringResource(R.string.account_deletion_notice_irreversible),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun NoticeLine(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text = text,
        color = color,
        style = HopesTextStyles.Label,
    )
}

// 피그마 32-6: 라벨과 입력란, 입력란과 오류 문구 사이 6.
private val LABEL_FIELD_GAP = 6.dp

@Preview(showBackground = true, name = "32-6 회원탈퇴")
@Composable
private fun AccountDeletionScreenPreview() {
    HopesTheme {
        AccountDeletionScreen(
            password = "",
            onPasswordChange = {},
            errorMessage = null,
            isDeleting = false,
            isConfirmDialogVisible = false,
            onSubmitClick = {},
            onConfirmDismiss = {},
            onConfirm = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "32-8 회원탈퇴 비밀번호 오류")
@Composable
private fun AccountDeletionScreenErrorPreview() {
    HopesTheme {
        AccountDeletionScreen(
            password = "wrong-password",
            onPasswordChange = {},
            errorMessage = "비밀번호가 맞지 않아요. 다시 확인해 주세요.",
            isDeleting = false,
            isConfirmDialogVisible = false,
            onSubmitClick = {},
            onConfirmDismiss = {},
            onConfirm = {},
            onBackClick = {},
        )
    }
}
