package com.example.hopes.feature.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesErrorBanner
import com.example.hopes.core.designsystem.component.HopesFieldLabel
import com.example.hopes.core.designsystem.component.HopesInlineErrorMessage
import com.example.hopes.core.designsystem.component.HopesPrimaryButton
import com.example.hopes.core.designsystem.component.HopesSubScreenFrame
import com.example.hopes.core.designsystem.component.HopesSurfaceCard
import com.example.hopes.core.designsystem.component.HopesTextArea
import com.example.hopes.core.designsystem.component.HopesTextField
import com.example.hopes.core.designsystem.component.figmaRaisedShadow
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/**
 * 피그마 29 문의하기 화면이다. 입력 중에는 이메일·내용 폼과 이메일 문의 안내를,
 * 전송에 성공하면 29-4 완료 안내를 보여준다.
 */
@Composable
fun ContactScreen(
    uiState: ContactUiState,
    onEvent: (ContactScreenEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    if (uiState.isSent) {
        HopesSubScreenFrame(
            title = stringResource(R.string.contact),
            onBackClick = onBackClick,
            bottomButton = {
                HopesPrimaryButton(
                    text = stringResource(R.string.contact_sent_confirm),
                    onClick = onBackClick,
                )
            },
        ) {
            ContactSentMessage(
                email = uiState.email.trim(),
                modifier = Modifier.padding(top = SENT_MESSAGE_TOP_PADDING),
            )
        }
        return
    }

    HopesSubScreenFrame(
        title = stringResource(R.string.contact),
        subtitle = stringResource(R.string.contact_subtitle),
        onBackClick = onBackClick,
        bottomButton = {
            HopesPrimaryButton(
                text = stringResource(R.string.send_contact),
                onClick = { onEvent(ContactScreenEvent.ContactSendClicked) },
                isEnabled = !uiState.isSending,
            )
        },
    ) {
        if (uiState.isSendFailed) {
            HopesErrorBanner(message = stringResource(R.string.contact_send_failed))

            Spacer(modifier = Modifier.height(AppSpacing.Item))
        }

        val emailError = if (uiState.isEmailError) stringResource(R.string.contact_email_error) else null
        val messageError = if (uiState.isMessageError) stringResource(R.string.contact_message_error) else null

        HopesSurfaceCard {
            HopesFieldLabel(text = stringResource(R.string.contact_reply_email))

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesTextField(
                value = uiState.email,
                onValueChange = { onEvent(ContactScreenEvent.ContactEmailChanged(it)) },
                placeholder = stringResource(R.string.contact_email_placeholder),
                accessibilityLabel = stringResource(R.string.contact_reply_email),
                errorMessage = emailError,
                isEnabled = !uiState.isSending,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            )

            if (emailError != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Small + AppSpacing.XSmall))

                HopesInlineErrorMessage(message = emailError)
            }

            Spacer(modifier = Modifier.height(AppSpacing.Item))

            HopesFieldLabel(text = stringResource(R.string.contact_content))

            Spacer(modifier = Modifier.height(AppSpacing.Compact))

            HopesTextArea(
                value = uiState.message,
                onValueChange = { onEvent(ContactScreenEvent.ContactMessageChanged(it)) },
                placeholder = stringResource(R.string.contact_placeholder),
                accessibilityLabel = stringResource(R.string.contact_content),
                errorMessage = messageError,
                isEnabled = !uiState.isSending,
            )

            if (messageError != null) {
                Spacer(modifier = Modifier.height(AppSpacing.Small + AppSpacing.XSmall))

                HopesInlineErrorMessage(message = messageError)
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.Item))

        ContactEmailInfoCard()
    }
}

@Composable
private fun ContactEmailInfoCard() {
    HopesSurfaceCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .size(INFO_ICON_CONTAINER_SIZE)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(AppRadius.SmallButton),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.MailOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(AppIconSize.Small),
                )
            }

            Spacer(modifier = Modifier.width(AppSpacing.Item))

            Text(
                text = stringResource(R.string.contact_email_info),
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.LabelStrong,
            )
        }
    }
}

@Composable
private fun ContactSentMessage(
    email: String,
    modifier: Modifier = Modifier,
) {
    val title =
        buildAnnotatedString {
            append(stringResource(R.string.contact_sent_title_prefix))
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(stringResource(R.string.contact_sent_title_highlight))
            }
        }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(SENT_ICON_CIRCLE_SIZE)
                    .figmaRaisedShadow(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.MailOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(SENT_ICON_SIZE),
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.Medium))

        Text(
            text = title,
            modifier = Modifier.semantics { heading() },
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Headline,
        )

        Spacer(modifier = Modifier.height(AppSpacing.Compact))

        Text(
            text = stringResource(R.string.contact_sent_description, email),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = HopesTextStyles.Subtitle,
        )
    }
}

// 피그마 29 이메일 안내 아이콘 배경, 29-4 완료 원형 아이콘 크기와 위 여백이다.
private val INFO_ICON_CONTAINER_SIZE = 32.dp
private val SENT_ICON_CIRCLE_SIZE = 64.dp
private val SENT_ICON_SIZE = 28.dp
private val SENT_MESSAGE_TOP_PADDING = 96.dp

@Preview(showBackground = true, name = "29 문의하기")
@Composable
private fun ContactScreenPreview() {
    HopesTheme {
        ContactScreen(
            uiState = ContactUiState(),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "29-3 문의하기 입력 오류")
@Composable
private fun ContactScreenErrorPreview() {
    HopesTheme {
        ContactScreen(
            uiState = ContactUiState(email = "s26055@gsm", isEmailError = true, isMessageError = true),
            onEvent = {},
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true, name = "29-4 문의 전송 완료")
@Composable
private fun ContactScreenSentPreview() {
    HopesTheme {
        ContactScreen(
            uiState = ContactUiState(email = "s26055@gsm.hs.kr", isSent = true),
            onEvent = {},
            onBackClick = {},
        )
    }
}
