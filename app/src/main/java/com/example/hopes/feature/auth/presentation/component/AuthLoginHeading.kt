package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 로그인 화면의 "로그인" 제목과 안내 부제목이다. */
@Composable
fun AuthLoginHeading(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.login),
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.ScreenTitle,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Small))

        Text(
            text = stringResource(R.string.login_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = HopesTextStyles.Subtitle,
        )
    }
}
