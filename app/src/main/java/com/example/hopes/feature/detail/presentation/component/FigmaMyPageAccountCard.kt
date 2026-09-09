package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.Arrangement

import com.example.hopes.R

/** 마이페이지 계정 정보 카드다. */
@Composable
fun FigmaMyPageAccountCard(
    email: String,
    major: String?,
    modifier: Modifier = Modifier,
) {
    FigmaDetailCard(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp),
        shadowStyle = FigmaDetailCardShadow.Raised,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,

        ) {
            Text(
                text = stringResource(R.string.my_page_account),
                style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(17.dp))

            Text(
                text = stringResource(R.string.my_page_email, email.ifBlank { "-" }),
                style = TextStyle(fontSize = 13.sp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.my_page_major, major ?: "-"),
                style = TextStyle(fontSize = 13.sp),
            )
        }
    }
}
