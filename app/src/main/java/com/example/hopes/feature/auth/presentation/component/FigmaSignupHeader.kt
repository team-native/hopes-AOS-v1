package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaBrandHeader
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 회원가입 폼 위에 고정되는 파란 그라데이션 헤더다. */
@Composable
fun FigmaSignupHeader() {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        extendedColors.signupGradientStart,
                        extendedColors.signupGradientEnd,
                    ),
                ),
            )
            .padding(start = 32.dp, top = 76.dp),
    ) {
        FigmaBrandHeader(
            isOnBlueBackground = true,
        )

        Text(
            text = stringResource(R.string.signup_hero_title),
            modifier = Modifier
                .padding(top = 78.dp)
                .width(260.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 35.sp),
        )
    }
}
