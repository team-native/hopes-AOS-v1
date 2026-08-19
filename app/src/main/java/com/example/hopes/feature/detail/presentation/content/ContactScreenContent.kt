package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.offset
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
        FigmaDetailBackHeader(
            title = stringResource(R.string.contact),
            subtitle = stringResource(R.string.contact_subtitle),
            onBackClick = { onEvent(DetailScreenEvent.BackClicked) },
        )

        // 헤더의 레이아웃 높이(150dp)가 실제 보이는 내용보다 넉넉히 잡혀 있어, 폼 카드가 그
        // 여백 안쪽(y=107dp)에서 시작한다. Column 순차 배치로는 이 겹침을 표현할 수 없어
        // 화면 상단 기준 offset을 유지한다. offset은 padding과 달리 음수에서도 안전하다.
        FigmaContactFormCard(
            contactEmail = uiState.contactEmail,
            contactMessage = uiState.contactMessage,
            isContactSent = uiState.isContactSent,
            onContactEmailChange = { onEvent(DetailScreenEvent.ContactEmailChanged(it)) },
            onContactMessageChange = { onEvent(DetailScreenEvent.ContactMessageChanged(it)) },
            onSendClick = { onEvent(DetailScreenEvent.ContactSendClicked) },
            modifier = Modifier
                .padding(start = 24.dp)
                .offset(y = 107.dp),
        )

        FigmaContactInformationCard(
            modifier = Modifier
                .padding(start = 24.dp)
                .offset(y = 624.dp),
        )
    }
}
