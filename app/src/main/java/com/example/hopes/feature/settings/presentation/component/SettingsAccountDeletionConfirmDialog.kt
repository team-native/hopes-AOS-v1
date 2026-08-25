package com.example.hopes.feature.settings.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R

/** 입력한 비밀번호로 회원탈퇴를 최종 확인하는 Dialog다. */
@Composable
fun SettingsAccountDeletionConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.account_deletion_confirm_title))
        },
        text = {
            Text(text = stringResource(R.string.account_deletion_confirm_message))
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.account_deletion_cancel))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.account_deletion_confirm),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}
