package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 카드 입력값 위에 표시하는 Figma의 작은 필드 라벨이다. */
@Composable
fun FigmaDetailFieldLabel(
    text: String,
    modifier: Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
    )
}

/** 프로필 이름에 사용하는 306×40 단일 행 입력 필드다. */
@Composable
fun FigmaDetailThinInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .width(306.dp)
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 15.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 16.dp, top = 9.dp)
                .width(274.dp)
                .height(24.dp),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}

/** 문의 카드에서 원본과 같은 가로 폭을 사용하는 이메일 입력 필드다. */
@Composable
fun FigmaDetailContactEmailInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Box(
        modifier = Modifier
            .padding(start = 18.dp, top = 67.dp)
            .width(332.dp)
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, extendedColors.authFieldBorder, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 15.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 16.dp, top = 9.dp)
                .width(300.dp)
                .height(24.dp),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
    }
}

/** 프로필·개인 설정·문의에서 공통으로 쓰는 여러 줄 입력 필드다. */
@Composable
fun FigmaDetailTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    height: Int,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .width(306.dp)
            .height(height.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                modifier = Modifier
                    .padding(start = 16.dp, top = 18.dp)
                    .width(260.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = TextStyle(fontSize = 15.sp, lineHeight = 22.sp),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .padding(start = 16.dp, top = 14.dp)
                .width(274.dp)
                .height((height - 28).dp),
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp,
            ),
        )
    }
}
