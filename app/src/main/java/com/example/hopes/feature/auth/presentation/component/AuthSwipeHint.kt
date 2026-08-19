package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.HopesExtendedColors

/**
 * 안내 시트가 접힌 위치 바로 위에 위로 스와이프하라는 화살표·문구를 띄운다. 시트 위치와 무관한
 * 정적 콘텐츠라 offset이 아닌 일반 레이아웃 흐름으로 배치한다.
 */
@Composable
fun AuthSwipeHint(
    extendedColors: HopesExtendedColors,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.figma_auth_swipe_arrow_two),
                contentDescription = null,
                modifier = Modifier
                    .width(22.dp)
                    .height(39.dp)
                    .rotate(90f),
            )
            Image(
                painter = painterResource(R.drawable.figma_auth_swipe_arrow_one),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .width(22.dp)
                    .height(39.dp)
                    .rotate(90f),
            )
        }

        Text(
            text = stringResource(R.string.auth_swipe_title),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.auth_swipe),
            color = extendedColors.authDescription,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 12.sp),
        )
    }
}
