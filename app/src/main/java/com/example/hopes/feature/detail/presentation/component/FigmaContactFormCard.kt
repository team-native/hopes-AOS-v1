package com.example.hopes.feature.detail.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hopes.R

/** 피그마 14 문의 이메일·내용 입력과 전송 버튼을 포함한 카드다. */
@Composable
fun FigmaContactFormCard(
    contactEmail: String,
    contactMessage: String,
    isContactSent: Boolean,
    onContactEmailChange: (String) -> Unit,
    onContactMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FigmaDetailCard(
        modifier = modifier
            .width(354.dp)
            .height(420.dp),
        shadowStyle = FigmaDetailCardShadow.Subtle,
    ) {
        Column(modifier = Modifier.padding(start = 24.dp, top = 42.dp)) {
            FigmaDetailFieldLabel(text = stringResource(R.string.email))

            Spacer(modifier = Modifier.height(9.dp))

            FigmaDetailContactEmailInput(
                value = contactEmail,
                onValueChange = onContactEmailChange,
                hint = stringResource(R.string.email),
            )

            Spacer(modifier = Modifier.height(29.dp))

            FigmaDetailFieldLabel(text = stringResource(R.string.contact_content))

            Spacer(modifier = Modifier.height(8.dp))

            FigmaDetailTextArea(
                value = contactMessage,
                onValueChange = onContactMessageChange,
                hint = stringResource(R.string.contact_placeholder),
                height = 154,
            )

            Spacer(modifier = Modifier.height(30.dp))

            FigmaDetailPrimaryButton(
                text = if (isContactSent) {
                    stringResource(R.string.sent)
                } else {
                    stringResource(R.string.send_contact)
                },
                modifier = Modifier
                    .width(306.dp)
                    .height(48.dp),
                onClick = onSendClick,
            )
        }
    }
}
