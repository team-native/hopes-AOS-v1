package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R

/** 인증 단계 화면(비밀번호 재설정 등)에서 쓰는, 배경·테두리 없이 화살표만 보이는 뒤로가기 버튼이다. */
@Composable
fun AuthStepBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            // 터치 영역(40dp)이 아이콘(20dp)보다 넓어 생기는 여백만큼 왼쪽으로 당겨, 화살표가 화면 좌우 여백 기준선에 맞도록 한다.
            .offset(x = (-10).dp),
    ) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = stringResource(R.string.back),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp),
        )
    }
}
