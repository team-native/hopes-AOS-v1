package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    isPromptLoadFailed: Boolean,
    isPromptSaving: Boolean,
    isPromptSaved: Boolean,
    isPromptSaveFailed: Boolean,
    onPersonalPromptChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FigmaDetailCard(
        modifier = modifier
            .width(354.dp)
            .height(408.dp),
        shadowStyle = FigmaDetailCardShadow.Subtle,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp),
        ) {
            Text(
                text = stringResource(R.string.personal_settings),
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(26.dp))

            FigmaDetailFieldLabel(text = stringResource(R.string.system_prompt_description))

            Spacer(modifier = Modifier.height(18.dp))

            FigmaDetailTextArea(
                value = personalPrompt,
                onValueChange = onPersonalPromptChange,
                hint = stringResource(R.string.prompt_placeholder),
                height = 210,
            )

            Spacer(modifier = Modifier.height(19.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                FigmaDetailPrimaryButton(
                    text = when {
                        isPromptLoading -> stringResource(R.string.prompt_loading)
                        isPromptLoadFailed -> stringResource(R.string.prompt_load_error)
                        isPromptSaving -> stringResource(R.string.prompt_saving)
                        isPromptSaved -> stringResource(R.string.saved)
                        isPromptSaveFailed -> stringResource(R.string.prompt_save_error)
                        else -> stringResource(R.string.prompt_save)
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(46.dp),
                    onClick = onSaveClick,
                )
            }
        }
    }
}
