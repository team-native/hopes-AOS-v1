package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 로그인 시트 뒤에서 히어로 영역을 은은하게 어둡히는 스크림이다. */
@Composable
fun AuthBackdropScrim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(397.dp)
            .background(LocalHopesExtendedColors.current.authBackdropScrim),
    )
}
