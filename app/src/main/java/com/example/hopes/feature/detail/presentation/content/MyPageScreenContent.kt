package com.example.hopes.feature.detail.presentation.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.FigmaAppFrame
import com.example.hopes.core.designsystem.component.HopesHeaderIconButton
import com.example.hopes.core.designsystem.component.HopesToast
import com.example.hopes.feature.detail.presentation.MyPageActivityStats
import com.example.hopes.feature.detail.presentation.MyPageScreenEvent
import com.example.hopes.feature.detail.presentation.MyPageUiState
import com.example.hopes.feature.detail.presentation.component.MyPageActivityStatsCard
import com.example.hopes.feature.detail.presentation.component.MyPageMenuCard
import com.example.hopes.feature.detail.presentation.component.MyPageMenuItem
import com.example.hopes.feature.detail.presentation.component.MyPageProfileSummaryCard
import com.example.hopes.navigation.HopesDestination
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.HopesTheme
import com.teamnative.hopes.R

/** 피그마 31 마이페이지다. 프로필 요약, 활동 통계, 하위 화면 메뉴를 위에서부터 쌓는다. */
@Composable
fun MyPageScreenContent(
    uiState: MyPageUiState,
    onEvent: (MyPageScreenEvent) -> Unit,
    onNavigate: (HopesDestination) -> Unit,
    toastMessage: String? = null,
    onToastDismiss: () -> Unit = {},
) {
    val menuItems =
        listOf(
            MyPageMenuItem(
                icon = Icons.Outlined.Edit,
                title = stringResource(R.string.my_page_menu_activity),
                description = stringResource(R.string.my_page_menu_activity_description),
                onClick = { onEvent(MyPageScreenEvent.MyActivityClicked) },
            ),
            MyPageMenuItem(
                icon = Icons.Outlined.Tune,
                title = stringResource(R.string.personal_settings),
                description = stringResource(R.string.my_page_menu_personal_settings_description),
                onClick = { onEvent(MyPageScreenEvent.PersonalSettingsClicked) },
            ),
            MyPageMenuItem(
                icon = Icons.Outlined.Notifications,
                title = stringResource(R.string.my_page_menu_notification),
                description = stringResource(R.string.my_page_menu_notification_description),
                onClick = { onEvent(MyPageScreenEvent.NotificationClicked) },
            ),
            MyPageMenuItem(
                icon = Icons.Outlined.MailOutline,
                title = stringResource(R.string.contact),
                description = stringResource(R.string.settings_contact_description),
                onClick = { onEvent(MyPageScreenEvent.ContactClicked) },
            ),
        )

    FigmaAppFrame(
        selectedDestination = HopesDestination.Settings,
        onNavigate = onNavigate,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = AppSpacing.ScreenHorizontal,
                            top = AppSpacing.SystemBarToContent,
                            end = AppSpacing.ScreenHorizontal,
                            bottom = AppSpacing.Section,
                        ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.my_page),
                        modifier =
                            Modifier
                                .weight(1f)
                                .semantics { heading() },
                        color = MaterialTheme.colorScheme.onSurface,
                        style = HopesTextStyles.PageTitle,
                    )

                    HopesHeaderIconButton(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = stringResource(R.string.navigation_settings),
                        onClick = { onEvent(MyPageScreenEvent.AppSettingsClicked) },
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.Medium))

                MyPageProfileSummaryCard(
                    name = uiState.profileName,
                    major = uiState.major,
                    email = uiState.email,
                    onProfileEditClick = { onEvent(MyPageScreenEvent.ProfileEditClicked) },
                )

                uiState.activityStats?.let { stats ->
                    Spacer(modifier = Modifier.height(AppSpacing.Item))

                    MyPageActivityStatsCard(stats = stats)
                }

                Spacer(modifier = Modifier.height(AppSpacing.Item))

                MyPageMenuCard(items = menuItems)
            }

            if (toastMessage != null) {
                HopesToast(
                    message = toastMessage,
                    onDismiss = onToastDismiss,
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = AppSpacing.Medium),
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "31 마이페이지")
@Composable
private fun MyPageScreenContentPreview() {
    HopesTheme {
        MyPageScreenContent(
            uiState =
                MyPageUiState(
                    profileName = "임서하",
                    email = "s26055@gsm.hs.kr",
                    major = "인공지능과",
                    isProfileLoading = false,
                    activityStats = MyPageActivityStats(postCount = 2, commentCount = 5, chatCount = 12),
                ),
            onEvent = {},
            onNavigate = {},
        )
    }
}
