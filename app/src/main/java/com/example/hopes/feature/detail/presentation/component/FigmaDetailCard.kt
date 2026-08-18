package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.core.designsystem.component.figmaSubtleShadow

/** 화면별 Figma 원본 카드에 맞는 그림자 역할이다. */
enum class FigmaDetailCardShadow {
    Raised,
    Subtle,
    None,
}

/** 상세 화면 카드의 공통 테두리·그림자 표현이다. 폭은 호출부의 modifier가 결정한다. */
@Composable
fun FigmaDetailCard(
    modifier: Modifier,
    shadowStyle: FigmaDetailCardShadow,
    shape: RoundedCornerShape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .then(
                when (shadowStyle) {
                    FigmaDetailCardShadow.Raised -> Modifier.figmaRaisedShadow(shape)
                    FigmaDetailCardShadow.Subtle -> Modifier.figmaSubtleShadow(shape)
                    FigmaDetailCardShadow.None -> Modifier
                },
            )
            .background(MaterialTheme.colorScheme.surface, shape)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape),
    ) {
        content()
    }
}
