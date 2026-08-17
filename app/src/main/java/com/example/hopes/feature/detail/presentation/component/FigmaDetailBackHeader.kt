package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.figmaSubtleShadow

/** 상세·개인 설정·문의 화면에서 공통으로 쓰는 뒤로가기 상단 바다. */
@Composable
fun FigmaDetailBackHeader(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    backOffsetX: Int = 18,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = backOffsetX.dp, top = 25.dp, end = 24.dp)
                .height(39.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FigmaDetailBackButton(
                modifier = Modifier,
                onBackClick = onBackClick,
            )

            Spacer(modifier = Modifier.width(18.dp))

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

        Text(
            text = subtitle,
            modifier = Modifier.padding(start = 74.dp, top = 62.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = TextStyle(fontSize = 13.sp, lineHeight = 18.sp),
        )
    }
}

/** 피그마 38×38 뒤로가기 버튼의 그림자·테두리·접근성 역할을 보존한다. */
@Composable
private fun FigmaDetailBackButton(
    modifier: Modifier,
    onBackClick: () -> Unit,
) {
    val backDescription = stringResource(R.string.back)

    Box(
        modifier = modifier
            .width(38.dp)
            .height(38.dp)
            .figmaSubtleShadow(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(13.dp))
            .semantics {
                role = Role.Button
                contentDescription = backDescription
            }
            .clickable(onClick = onBackClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.width(24.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
