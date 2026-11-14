package com.example.hopes.feature.auth.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 시작 화면 헤드라인 아래의 서비스 설명 문구다. */
@Composable
fun AuthStartDescription(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.auth_start_description),
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = HopesTextStyles.Description,
    )
}
