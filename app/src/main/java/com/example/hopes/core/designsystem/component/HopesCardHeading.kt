package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.ui.theme.HopesTextStyles

/** 카드 안쪽 맨 위의 굵은 제목과 선택적인 회색 설명 한 줄이다(예: "계정 정보 / 수정할 수 없는 정보예요."). */
@Composable
fun HopesCardHeading(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            style = HopesTextStyles.BodyStrong,
        )

        if (description != null) {
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Caption,
            )
        }
    }
}
