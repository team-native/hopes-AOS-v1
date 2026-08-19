package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 회원가입 카드 한 입력 행에서 라벨, 필드, 오류 문구를 세로로 배치한다. */
@Composable
fun SignupFormFieldGroup(
    labelRes: Int,
    errorMessage: String? = null,
    field: @Composable () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().heightIn(min = 66.dp)) {
        Text(
            text = stringResource(labelRes),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp),
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(6.dp))

        field()

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp),
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
            )
        }
    }
}
