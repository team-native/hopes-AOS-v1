package com.example.hopes.feature.detail.presentation.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.hopes.R
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.feature.detail.presentation.DetailScreenEvent
import com.example.hopes.feature.detail.presentation.DetailUiState
import com.example.hopes.feature.detail.presentation.component.FigmaContactFormCard
import com.example.hopes.feature.detail.presentation.component.FigmaContactInformationCard
import com.example.hopes.feature.detail.presentation.component.FigmaDetailBackHeader
import com.example.hopes.navigation.HopesDestination

/** 피그마 14 문의 폼과 이메일 안내 영역을 구성한다. */
@Composable
fun ContactScreenContent(
    uiState: DetailUiState,
    onEvent: (DetailScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
) {
    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        FigmaDetailBackHeader(
            title = stringResource(R.string.contact),
            subtitle = stringResource(R.string.contact_subtitle),
            onBackClick = { onEvent(DetailScreenEvent.BackClicked) },
        )
        FigmaContactFormCard(
            contactEmail = uiState.contactEmail,
            contactMessage = uiState.contactMessage,
            isContactSent = uiState.isContactSent,
            onContactEmailChange = { onEvent(DetailScreenEvent.ContactEmailChanged(it)) },
            onContactMessageChange = { onEvent(DetailScreenEvent.ContactMessageChanged(it)) },
            onSendClick = { onEvent(DetailScreenEvent.ContactSendClicked) },
        )
        FigmaContactInformationCard()
    }
}
