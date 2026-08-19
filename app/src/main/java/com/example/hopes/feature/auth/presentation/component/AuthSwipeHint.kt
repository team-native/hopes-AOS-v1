package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
 * 안내 시트가 접힌 위치(sheetTopOffset) 위로 위로 스와이프하라는 화살표·문구를 띄운다.
 * 접힌 위치는 화면 높이에 따라 달라지는 값이라, 각 자식은 그 값을 기준으로 한 상대
 * offset으로 배치한다. offset은 padding과 달리 음수를 허용해 작은 화면에서도 크래시하지 않는다.
 */
@Composable
fun AuthSwipeHint(
    extendedColors: HopesExtendedColors,
    sheetTopOffset: Float,
) {
    Image(
        painter = painterResource(R.drawable.figma_auth_swipe_arrow_two),
        contentDescription = null,
        modifier = Modifier
            .offset(x = 181.dp, y = (sheetTopOffset - 151f).dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Image(
        painter = painterResource(R.drawable.figma_auth_swipe_arrow_one),
        contentDescription = null,
        modifier = Modifier
            .offset(x = 181.dp, y = (sheetTopOffset - 133f).dp)
            .width(22.dp)
            .height(39.dp)
            .rotate(90f),
    )
    Text(
        text = stringResource(R.string.auth_swipe_title),
        modifier = Modifier
            .offset(y = (sheetTopOffset - 93f).dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        ),
    )
    Text(
        text = stringResource(R.string.auth_swipe),
        modifier = Modifier
            .offset(y = (sheetTopOffset - 63f).dp)
            .fillMaxWidth(),
        color = extendedColors.authDescription,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 12.sp),
    )
}
