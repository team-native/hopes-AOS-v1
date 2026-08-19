package com.example.hopes.feature.chat.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaRaisedShadow

/** 채팅 홈의 로고, 환영 문구, 서버 대화 생성 오류 안내를 세로로 배치한다. */
@Composable
fun ChatWelcomeHero(
    isCreateChatError: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .figmaRaisedShadow(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(44.dp)
                    .width(30.dp),
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.chat_welcome),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.chat_welcome_description),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 14.sp),
        )

        if (isCreateChatError) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.chat_create_error),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
            )
        }
    }
}
