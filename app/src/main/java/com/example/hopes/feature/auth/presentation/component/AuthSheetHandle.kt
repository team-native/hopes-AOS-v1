package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 드래그 가능한 인증 시트 상단의 손잡이 막대다. */
@Composable
fun AuthSheetHandle(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .width(86.dp)
                .height(5.dp)
                .background(
                    color = LocalHopesExtendedColors.current.authHandle,
                    shape = RoundedCornerShape(3.dp),
                ),
        )
    }
}
