package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.component.FigmaBackButton
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 인증 하위 단계 화면(비밀번호 재설정, 이메일 인증 등)이 공통으로 쓰는 뒤로가기와 제목 영역이다. */
@Composable
fun AuthStepHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        FigmaBackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            color = LocalHopesExtendedColors.current.authSubtitle,
            style = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
        )
    }
}
