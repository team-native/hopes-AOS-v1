package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 문의 전송 방법을 보완하는 하단 이메일 안내 카드다. 배치는 호출부가 맡는다. */
@Composable
fun FigmaContactInformationCard(modifier: Modifier = Modifier) {
    FigmaDetailCard(
        modifier = modifier
            .width(354.dp)
            .height(70.dp),
        shadowStyle = FigmaDetailCardShadow.None,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = stringResource(R.string.contact_email_info),
            modifier = Modifier.padding(start = 24.dp, top = 25.dp),
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}
