package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.component.figmaLoginLogoShadow
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 피그마 인증 화면의 42dp 브랜드 헤더다. */
@Composable
fun FigmaAuthBrandHeader(
    modifier: Modifier = Modifier,
    logoShadowStyle: FigmaAuthLogoShadowStyle = FigmaAuthLogoShadowStyle.Raised,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .then(
                    if (logoShadowStyle == FigmaAuthLogoShadowStyle.Login) {
                        Modifier.figmaLoginLogoShadow(RoundedCornerShape(AppRadius.Logo))
                    } else {
                        Modifier.figmaRaisedShadow(RoundedCornerShape(AppRadius.Logo))
                    },
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(AppRadius.Logo),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.icon),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(26.dp)
                    .width(18.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )

            Text(
                text = stringResource(R.string.school_name),
                color = extendedColors.brandSubtitleOnBlue,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

/** 인증 화면별 브랜드 로고에 사용되는 Figma 그림자 규격이다. */
enum class FigmaAuthLogoShadowStyle {
    Raised,
    Login,
}
