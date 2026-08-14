package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 상단의 42dp 브랜드 마크와 두 줄 서비스명을 동일한 비율로 표시한다. */
@Composable
fun FigmaBrandHeader(
    modifier: Modifier = Modifier,
    isOnBlueBackground: Boolean = false,
    logoShadow: FigmaBrandLogoShadow = FigmaBrandLogoShadow.Raised,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val titleColor = if (isOnBlueBackground) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val subtitleColor = if (isOnBlueBackground) {
        extendedColors.brandSubtitleOnBlue
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .width(250.dp)
            .height(42.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                // 화면별로 Figma 원본에 지정된 로고 그림자 값을 선택한다.
                .then(
                    if (logoShadow == FigmaBrandLogoShadow.Subtle) {
                        Modifier.figmaSubtleShadow(RoundedCornerShape(12.dp))
                    } else {
                        Modifier.figmaRaisedShadow(RoundedCornerShape(12.dp))
                    },
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.logo_mark),
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = 54.dp, top = 4.dp),
            color = titleColor,
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
            ),
        )

        Text(
            text = stringResource(R.string.school_name),
            modifier = Modifier.padding(start = 54.dp, top = 25.dp),
            color = subtitleColor,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

/** 상단 브랜드 마크에 적용되는 Figma 화면별 그림자 규격이다. */
enum class FigmaBrandLogoShadow {
    Raised,
    Subtle,
}
