package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R
import com.example.hopes.feature.auth.presentation.component.FigmaAuthTextField
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 현재 비밀번호를 입력받는 회원탈퇴 BottomSheet다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAccountDeletionBottomSheet(
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMessage: String?,
    isDeletingAccount: Boolean,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val extendedColors = LocalHopesExtendedColors.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.account_deletion_title),
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = stringResource(R.string.account_deletion_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = stringResource(R.string.account_deletion_password_prompt),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )

            FigmaAuthTextField(
                value = password,
                onValueChange = onPasswordChange,
                labelRes = R.string.account_deletion_password_hint,
                isPassword = true,
                onImeAction = {
                    if (password.isNotBlank() && !isDeletingAccount) {
                        onDeleteClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isDeletingAccount,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.account_deletion_cancel))
                }

                Button(
                    onClick = onDeleteClick,
                    enabled = password.isNotBlank() && !isDeletingAccount,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = extendedColors.logoutContainer,
                        contentColor = extendedColors.logoutText,
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    if (isDeletingAccount) {
                        CircularProgressIndicator(
                            color = extendedColors.logoutText,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(text = stringResource(R.string.account_deletion_confirm))
                    }
                }
            }
        }
    }
}
