package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaBackButton

/** 상세·개인 설정·문의 화면에서 공통으로 쓰는 뒤로가기 상단 바다. */
@Composable
fun FigmaDetailBackHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    backOffsetX: Int = 18,
    applySystemBarPadding: Boolean = false,
    subtitleSpacing: Dp = 8.dp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (applySystemBarPadding) Modifier.statusBarsPadding() else Modifier)
            .padding(top = if (applySystemBarPadding) 0.dp else 25.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = backOffsetX.dp, end = 24.dp)
                .height(39.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FigmaBackButton(
                onClick = onBackClick,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                ),
            )

            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.width(12.dp))

                FigmaDetailTopAction(
                    text = actionText,
                    modifier = Modifier,
                    onClick = onActionClick,
                )
            }
        }

        // 시스템바 패딩을 적용하는 화면은 헤더 행 바로 아래에 subtitleSpacing만큼만 띄운다.
        // 적용하지 않는 화면은 기존 좌표상 그 간격이 사실상 0에 가까워 별도 간격을 두지 않는다.
        Spacer(modifier = Modifier.height(if (applySystemBarPadding) subtitleSpacing else 0.dp))

        Text(
            text = subtitle,
            modifier = Modifier.padding(start = 74.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
        )
    }
}
