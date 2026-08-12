package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 이름과 개인화 소개를 편집하는 마이페이지 프로필 카드다. */
@Composable
fun FigmaMyPageProfileCard(
    profileName: String,
    profileIntroduction: String,
    onProfileNameChange: (String) -> Unit,
    onProfileIntroductionChange: (String) -> Unit,
) {
    FigmaDetailCard(
        modifier = Modifier
            .offset(x = 12.dp, y = 350.dp)
            .height(326.dp),
        shadowStyle = FigmaDetailCardShadow.Raised,
    ) {
        Text(
            text = stringResource(R.string.profile),
            modifier = Modifier.offset(x = 24.dp, y = 32.dp),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        )
        FigmaDetailFieldLabel(
            text = stringResource(R.string.name),
            modifier = Modifier.offset(x = 24.dp, y = 80.dp),
        )
        FigmaDetailThinInput(
            value = profileName,
            onValueChange = onProfileNameChange,
            hint = stringResource(R.string.name),
            modifier = Modifier.offset(x = 24.dp, y = 101.dp),
        )
        FigmaDetailFieldLabel(
            text = stringResource(R.string.profile_personalization_label),
            modifier = Modifier.offset(x = 24.dp, y = 172.dp),
        )
        FigmaDetailTextArea(
            value = profileIntroduction,
            onValueChange = onProfileIntroductionChange,
            hint = stringResource(R.string.profile_placeholder),
            height = 92,
            modifier = Modifier.offset(x = 24.dp, y = 204.dp),
        )
    }
}
