package com.example.hopes.feature.settings.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.hopes.core.designsystem.component.FigmaBackButton

/** 설정 화면 상단의 뒤로가기, 제목, 설명 영역이다. */
@Composable
fun SettingsHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.padding(start = 18.dp, top = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FigmaBackButton(
                onClick = onBackClick,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.settings_title),
                style = TextStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp),
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = stringResource(R.string.logout_description),
            modifier = Modifier.padding(start = 74.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp),
        )
    }
}
