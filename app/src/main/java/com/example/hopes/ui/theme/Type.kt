package com.example.hopes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.teamnative.hopes.R

/** 피그마 v2 기본 폰트다(SIL OFL 1.1, docs/licenses/Pretendard-OFL.txt). 앱에서 쓰는 네 굵기만 넣었다. */
val Pretendard =
    FontFamily(
        Font(R.font.pretendard_regular, FontWeight.Normal),
        Font(R.font.pretendard_medium, FontWeight.Medium),
        Font(R.font.pretendard_semibold, FontWeight.SemiBold),
        Font(R.font.pretendard_bold, FontWeight.Bold),
    )

val HopesTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 34.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
    ).withFontFamily(Pretendard)

/** 따로 정하지 않은 Material 슬롯(버튼·입력란 기본 글꼴 등)까지 모두 [fontFamily]를 쓰게 한다. */
private fun Typography.withFontFamily(fontFamily: FontFamily): Typography =
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily),
    )

/**
 * Material Typography 슬롯에 없는 피그마 v2 텍스트 스타일이다.
 * 색은 담지 않으므로 Text의 color로 따로 지정한다.
 */
object HopesTextStyles {
    val PageTitle =
        TextStyle(fontFamily = Pretendard, fontSize = 24.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
    val Headline =
        TextStyle(fontFamily = Pretendard, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 28.sp)
    val SectionTitle =
        TextStyle(fontFamily = Pretendard, fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp)
    val Body = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, lineHeight = 20.sp)
    val BodyStrong =
        TextStyle(fontFamily = Pretendard, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
    val Hero = TextStyle(fontFamily = Pretendard, fontSize = 30.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)
    val ScreenTitle =
        TextStyle(fontFamily = Pretendard, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
    val CodeDigit = TextStyle(fontFamily = Pretendard, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    val SheetTitle = TextStyle(fontFamily = Pretendard, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    val TopBarTitle = TextStyle(fontFamily = Pretendard, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    val DialogTitle = TextStyle(fontFamily = Pretendard, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    val Field = TextStyle(fontFamily = Pretendard, fontSize = 15.sp)
    val FieldStrong = TextStyle(fontFamily = Pretendard, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    val Description = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, lineHeight = 22.sp)
    val Button = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    val Subtitle = TextStyle(fontFamily = Pretendard, fontSize = 13.sp, lineHeight = 18.sp)
    val Label = TextStyle(fontFamily = Pretendard, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    val LabelStrong = TextStyle(fontFamily = Pretendard, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    val Caption = TextStyle(fontFamily = Pretendard, fontSize = 12.sp, lineHeight = 16.sp)
    val CaptionStrong =
        TextStyle(fontFamily = Pretendard, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 16.sp)
    val Overline = TextStyle(fontFamily = Pretendard, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    val Tag = TextStyle(fontFamily = Pretendard, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    val NavigationLabel = TextStyle(fontFamily = Pretendard, fontSize = 10.sp, fontWeight = FontWeight.Medium)
}
