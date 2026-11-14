package com.example.hopes.feature.home.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.HopesHeaderIconButton
import com.example.hopes.ui.theme.HopesTextStyles
import com.teamnative.hopes.R

/** 홈 상단의 "서하님, 반가워요" 인사말과 부제목, 알림 버튼이다. */
@Composable
fun HomeGreetingHeader(
    userName: String?,
    hasUnreadNotification: Boolean,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text =
                    if (userName != null) {
                        stringResource(R.string.home_greeting, userName)
                    } else {
                        stringResource(R.string.home_greeting_without_name)
                    },
                modifier = Modifier.semantics { heading() },
                color = MaterialTheme.colorScheme.onSurface,
                style = HopesTextStyles.Headline,
            )

            Spacer(modifier = Modifier.height(AppSpacing.Small))

            Text(
                text = stringResource(R.string.home_greeting_subtitle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = HopesTextStyles.Subtitle,
            )
        }

        HopesHeaderIconButton(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = stringResource(R.string.home_notification),
            onClick = onNotificationClick,
            hasBadge = hasUnreadNotification,
        )
    }
}
