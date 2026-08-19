package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.hopes.R

/** 인증 단계 화면(비밀번호 재설정 등)에서 쓰는, 배경·테두리 없이 화살표만 보이는 뒤로가기 버튼이다. */
@Composable
fun AuthStepBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick, role = Role.Button),
        // 터치 영역(40dp)이 아이콘(20dp)보다 넓어 생기는 여백을, 아이콘을 중앙이 아닌 시작 쪽에
        // 정렬해서 흡수한다 — offset으로 박스를 당기는 대신, 아이콘이 화면 좌우 여백 기준선에
        // 그대로 맞도록 정렬만 바꾼다.
        contentAlignment = Alignment.CenterStart,
    ) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = stringResource(R.string.back),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp),
        )
    }
}
