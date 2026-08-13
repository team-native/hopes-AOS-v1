package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaBrandHeader

/** 피그마 10 마이페이지의 브랜드와 제목, 설정 진입 액션을 표시한다. */
@Composable
fun FigmaMyPageHeader(onAppSettingsClick: () -> Unit) {
    FigmaBrandHeader(modifier = Modifier.padding(start = 24.dp, top = 70.dp))
    Text(
        text = stringResource(R.string.my_page),
        modifier = Modifier.padding(start = 24.dp, top = 138.dp),
        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
    )
    FigmaDetailTopAction(
        text = stringResource(R.string.navigation_settings),
        modifier = Modifier.padding(start = 311.dp, top = 69.dp),
        onClick = onAppSettingsClick,
    )
}
