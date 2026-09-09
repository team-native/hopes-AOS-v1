package com.example.hopes.feature.settings.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 설정 화면에서 로그아웃과 회원탈퇴 동작을 하나의 계정 카드로 제공한다. */
@Composable
fun SettingsAccountSection(
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    isDeleteAccountEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.account),
            modifier = Modifier.padding(start = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column {
                SettingsAccountRow(
                    icon = Icons.AutoMirrored.Outlined.Logout,
                    textResId = R.string.logout,
                    onClick = onLogoutClick,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(start = 64.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.55f),
                )

                SettingsAccountRow(
                    icon = Icons.Outlined.PersonOff,
                    textResId = R.string.delete_account,
                    onClick = onDeleteAccountClick,
                    enabled = isDeleteAccountEnabled,
                )
            }
        }
    }
}

/** 계정 카드 안에서 아이콘, 메뉴명, 이동 Chevron을 같은 간격으로 배치한다. */
@Composable
private fun SettingsAccountRow(
    icon: ImageVector,
    @StringRes textResId: Int,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val accountActionColor = LocalHopesExtendedColors.current.logoutText

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .alpha(if (enabled) 1f else 0.5f)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = accountActionColor,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(textResId),
            modifier = Modifier.weight(1f),
            color = accountActionColor,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = accountActionColor,
        )
    }
}
