package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.height
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
            .padding(start = 12.dp, top = 350.dp)
            .height(326.dp),
        shadowStyle = FigmaDetailCardShadow.Raised,
    ) {
        Text(
            text = stringResource(R.string.profile),
            modifier = Modifier.padding(start = 24.dp, top = 32.dp),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        )
        FigmaDetailFieldLabel(
            text = stringResource(R.string.name),
            modifier = Modifier.padding(start = 24.dp, top = 80.dp),
        )
        FigmaDetailThinInput(
            value = profileName,
            onValueChange = onProfileNameChange,
            hint = stringResource(R.string.name),
            modifier = Modifier.padding(start = 24.dp, top = 101.dp),
        )
        FigmaDetailFieldLabel(
            text = stringResource(R.string.profile_personalization_label),
            modifier = Modifier.padding(start = 24.dp, top = 172.dp),
        )
        FigmaDetailTextArea(
            value = profileIntroduction,
            onValueChange = onProfileIntroductionChange,
            hint = stringResource(R.string.profile_placeholder),
            height = 92,
            modifier = Modifier.padding(start = 24.dp, top = 204.dp),
        )
    }
}
