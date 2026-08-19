package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        Column {
            FigmaDetailBackHeader(
                title = stringResource(R.string.contact),
                subtitle = stringResource(R.string.contact_subtitle),
                onBackClick = { onEvent(DetailScreenEvent.BackClicked) },
            )

            Spacer(modifier = Modifier.height(25.dp))

            FigmaContactFormCard(
                contactEmail = uiState.contactEmail,
                contactMessage = uiState.contactMessage,
                isContactSent = uiState.isContactSent,
                onContactEmailChange = { onEvent(DetailScreenEvent.ContactEmailChanged(it)) },
                onContactMessageChange = { onEvent(DetailScreenEvent.ContactMessageChanged(it)) },
                onSendClick = { onEvent(DetailScreenEvent.ContactSendClicked) },
                modifier = Modifier.padding(start = 24.dp),
            )

            Spacer(modifier = Modifier.height(97.dp))

            FigmaContactInformationCard(
                modifier = Modifier.padding(start = 24.dp),
            )
        }
    }
}
