package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 로그인 화면에서 비밀번호 재설정 단계로 이동하는 "비밀번호 찾기" 링크다. */
@Composable
fun AuthFindPasswordLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.auth_find_password),
        modifier =
            modifier
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                ).padding(
                    horizontal = AppSpacing.Small,
                    vertical = AppSpacing.Compact,
                ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = HopesTextStyles.Label,
    )
}
