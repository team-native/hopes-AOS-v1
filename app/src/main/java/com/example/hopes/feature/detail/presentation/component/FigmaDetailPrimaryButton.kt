package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.component.figmaSubtleShadow
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.shapeClickable

/** 상세 화면의 파란 저장·전송 버튼이다. */
@Composable
fun FigmaDetailPrimaryButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit,
    shadowStyle: FigmaDetailPrimaryButtonShadow = FigmaDetailPrimaryButtonShadow.Subtle,
) {
    val buttonShape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .then(
                if (shadowStyle == FigmaDetailPrimaryButtonShadow.Raised) {
                    Modifier.figmaRaisedShadow(buttonShape)
                } else {
                    Modifier.figmaSubtleShadow(buttonShape)
                },
            )
            .background(MaterialTheme.colorScheme.primary, buttonShape)
            .shapeClickable(shape = buttonShape, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        )
    }
}

/** 화면별 Figma 저장·전송 버튼에 적용되는 그림자 규격이다. */
enum class FigmaDetailPrimaryButtonShadow {
    Raised,
    Subtle,
}
