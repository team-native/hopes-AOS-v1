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
 * 피그마 v1.5 App 텍스트 스타일이다(폰트만 Pretendard, 510 굵기는 Medium).
 * 색은 담지 않으므로 Text의 color로 따로 지정한다.
 */
object HopesTextStyles {
    /** App/Display 28/38 Bold */
    val Display =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            letterSpacing = (-0.4).sp,
        )

    /** App/Title 22/30 Bold */
    val Title =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 30.sp,
            letterSpacing = (-0.3).sp,
        )

    /** App/Heading 18/26 Medium */
    val Heading =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 26.sp,
            letterSpacing = (-0.2).sp,
        )

    /** App/Body 15/24 Regular */
    val Body =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp,
        )

    /** App/Body Strong 15/24 Medium */
    val BodyStrong =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp,
        )

    /** App/Label 14/20 Medium */
    val Label = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp)

    /** App/Caption 12/16 Regular */
    val Caption = TextStyle(fontFamily = Pretendard, fontSize = 12.sp, lineHeight = 16.sp)

    /** App/Tab 11/14 Medium */
    val NavigationLabel =
        TextStyle(fontFamily = Pretendard, fontSize = 11.sp, fontWeight = FontWeight.Medium, lineHeight = 14.sp)

    // 아래는 기존 이름을 유지하는 별칭이다. 값은 위 피그마 스타일을 따른다.
    val PageTitle = Title
    val Headline = Title
    val ScreenTitle = Title
    val SectionTitle = Heading
    val LabelStrong = Label
    val Subtitle = Label
    val Description = Body
    val Field = Body
    val FieldStrong = BodyStrong
    val Button = BodyStrong
    val CaptionStrong = Label.copy(fontSize = 12.sp, lineHeight = 16.sp)
    val Overline = Caption
    val Tag = Caption

    /** 시작 화면 헤드라인이다. 피그마 01은 34/44 Semibold. */
    val Hero =
        TextStyle(
            fontFamily = Pretendard,
            fontSize = 34.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 44.sp,
            letterSpacing = (-0.4).sp,
        )

    // 피그마 텍스트 스타일에 대응하는 항목이 없는 기존 스타일이다(별도 확인 전까지 유지).
    val CodeDigit = TextStyle(fontFamily = Pretendard, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    val SheetTitle = TextStyle(fontFamily = Pretendard, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    val TopBarTitle = Heading
    val DialogTitle = TextStyle(fontFamily = Pretendard, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}
