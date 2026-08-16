package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 마이페이지 계정 정보 카드다. */
@Composable
fun FigmaMyPageAccountCard(
    modifier: Modifier = Modifier,
) {
    FigmaDetailCard(
        modifier = modifier
            .height(128.dp),
        shadowStyle = FigmaDetailCardShadow.Raised,
    ) {
        Text(
            text = stringResource(R.string.my_page_account),
            modifier = Modifier.padding(start = 24.dp, top = 26.dp),
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(R.string.my_page_email),
            modifier = Modifier.padding(start = 24.dp, top = 63.dp),
            style = TextStyle(fontSize = 13.sp),
        )
        Text(
            text = stringResource(R.string.my_page_major),
            modifier = Modifier.padding(start = 24.dp, top = 87.dp),
            style = TextStyle(fontSize = 13.sp),
        )
    }
}
