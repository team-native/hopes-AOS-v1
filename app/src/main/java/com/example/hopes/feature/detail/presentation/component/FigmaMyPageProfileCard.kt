package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier,
) {
    FigmaDetailCard(
        modifier = modifier
            .fillMaxWidth()
            .height(326.dp),
        shadowStyle = FigmaDetailCardShadow.Raised,
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)) {
            Text(
                text = stringResource(R.string.profile),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(26.dp))

            FigmaDetailFieldLabel(text = stringResource(R.string.name))

            Spacer(modifier = Modifier.height(7.dp))

            FigmaDetailThinInput(
                value = profileName,
                onValueChange = onProfileNameChange,
                hint = stringResource(R.string.name),
            )

            Spacer(modifier = Modifier.height(31.dp))

            FigmaDetailFieldLabel(text = stringResource(R.string.profile_personalization_label))

            Spacer(modifier = Modifier.height(18.dp))

            FigmaDetailTextArea(
                value = profileIntroduction,
                onValueChange = onProfileIntroductionChange,
                hint = stringResource(R.string.profile_placeholder),
                height = 92,
            )
        }
    }
}
