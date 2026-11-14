package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamnative.hopes.R
import java.util.Locale

/** 인증번호 남은 시간(mm:ss)과 "인증번호 다시 받기" 링크를 한 줄에 표시한다. 만료되면 시간이 빨간색이 된다. */
@Composable
fun SignupCodeTimerRow(
    remainingSeconds: Int,
    isExpired: Boolean,
    isResendEnabled: Boolean,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val timerText =
        String.format(
            Locale.ROOT,
            "%02d:%02d",
            remainingSeconds / SECONDS_PER_MINUTE,
            remainingSeconds % SECONDS_PER_MINUTE,
        )
    val timerColor =
        if (isExpired) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.primary
        }
    val timerDescription = stringResource(R.string.signup_code_remaining_time, timerText)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .semantics(mergeDescendants = true) { contentDescription = timerDescription },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = timerColor,
                modifier = Modifier.size(14.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = timerText,
                color = timerColor,
                style =
                    TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
            )
        }

        Text(
            text = stringResource(R.string.signup_resend_code),
            modifier =
                Modifier
                    .clickable(
                        enabled = isResendEnabled,
                        role = Role.Button,
                        onClick = onResendClick,
                    ).padding(horizontal = 4.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style =
                TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                ),
        )
    }
}

private const val SECONDS_PER_MINUTE = 60
