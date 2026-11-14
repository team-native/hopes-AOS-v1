package com.example.hopes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val HopesTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 34.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
    )

/**
 * Material Typography 슬롯에 없는 피그마 v2 텍스트 스타일이다.
 * 색은 담지 않으므로 Text의 color로 따로 지정한다.
 */
object HopesTextStyles {
    val Hero = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)
    val ScreenTitle = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
    val CodeDigit = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    val SheetTitle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    val TopBarTitle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
    val DialogTitle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
    val Field = TextStyle(fontSize = 15.sp)
    val FieldStrong = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    val Description = TextStyle(fontSize = 14.sp, lineHeight = 22.sp)
    val Button = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    val Subtitle = TextStyle(fontSize = 13.sp, lineHeight = 18.sp)
    val Label = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
    val LabelStrong = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    val Caption = TextStyle(fontSize = 12.sp, lineHeight = 16.sp)
    val CaptionStrong = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, lineHeight = 16.sp)
    val Overline = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium)
    val NavigationLabel = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium)
}
