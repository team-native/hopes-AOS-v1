package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 인증 안내·로그인 화면 상단의 타이틀과 설명 문구다. 배치는 호출부의 Column이 맡는다. */
@Composable
fun AuthHeroCopy(modifier: Modifier = Modifier) {
    val extendedColors = LocalHopesExtendedColors.current

    Column(modifier = modifier.width(318.dp)) {
        Text(
            text = stringResource(R.string.auth_title),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 39.sp,
            ),
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = stringResource(R.string.auth_description),
            color = extendedColors.authDescription,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
            ),
        )
    }
}
