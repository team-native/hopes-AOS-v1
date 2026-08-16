package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R

/** 피그마 13 개인 설정의 시스템 프롬프트 편집 카드다. */
@Composable
fun FigmaPersonalSettingsFormCard(
    personalPrompt: String,
    isPromptLoading: Boolean,
    isPromptLoadError: Boolean,
    isPromptSaving: Boolean,
    isPromptSaved: Boolean,
    isPromptSaveError: Boolean,
    onPersonalPromptChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    FigmaDetailCard(
        modifier = Modifier
            .padding(start = 24.dp, top = 158.dp)
            .height(408.dp),
        shadowStyle = FigmaDetailCardShadow.Subtle,
    ) {
        Text(
            text = stringResource(R.string.personal_settings),
            modifier = Modifier.padding(start = 24.dp, top = 32.dp),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        )
        FigmaDetailFieldLabel(
            text = stringResource(R.string.system_prompt_description),
            modifier = Modifier.padding(start = 24.dp, top = 80.dp),
        )
        FigmaDetailTextArea(
            value = personalPrompt,
            onValueChange = onPersonalPromptChange,
            hint = stringResource(R.string.prompt_placeholder),
            height = 210,
            modifier = Modifier.padding(start = 24.dp, top = 112.dp),
        )
        FigmaDetailPrimaryButton(
            text = when {
                isPromptLoading || isPromptSaving -> stringResource(R.string.profile_saving)
                isPromptLoadError -> stringResource(R.string.prompt_load_error)
                isPromptSaveError -> stringResource(R.string.prompt_save_error)
                isPromptSaved -> stringResource(R.string.saved)
                else -> stringResource(R.string.prompt_save)
            },
            modifier = Modifier
                .padding(start = 230.dp, top = 341.dp)
                .width(100.dp)
                .height(46.dp),
            enabled = !isPromptLoading && !isPromptSaving,
            onClick = onSaveClick,
        )
    }
}
