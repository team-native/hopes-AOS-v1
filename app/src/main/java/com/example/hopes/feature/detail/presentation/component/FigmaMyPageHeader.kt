package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaBrandHeader

/** 피그마 10 마이페이지의 브랜드와 제목, 설정 진입 액션을 표시한다. */
@Composable
fun FigmaMyPageHeader(
    onAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(start = 24.dp, end = 24.dp, top = AppSpacing.SystemBarToContent)) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            FigmaBrandHeader()

            FigmaDetailTopAction(
                text = stringResource(R.string.navigation_settings),
                modifier = Modifier,
                onClick = onAppSettingsClick,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.my_page),
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
        )
    }
}
