package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 설정 화면 하단의 로그아웃 버튼이다. */
@Composable
fun SettingsLogoutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(
                color = extendedColors.logoutContainer,
                shape = RoundedCornerShape(14.dp),
            )
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.logout),
            color = extendedColors.logoutText,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}
