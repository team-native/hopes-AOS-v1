package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.ui.theme.LocalHopesExtendedColors
import com.teamnative.hopes.R

/** 인증번호를 보낸 뒤 수정할 수 없게 고정된 이메일 표시란이다. "변경"을 누르면 이메일 입력으로 돌아간다. */
@Composable
fun SignupLockedEmailField(
    email: String,
    isChangeEnabled: Boolean,
    onChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color = extendedColors.replyFieldBackground,
                    shape = RoundedCornerShape(AppRadius.Field),
                ).padding(start = 16.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = email,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = 15.sp),
        )

        Text(
            text = stringResource(R.string.signup_change_email),
            modifier =
                Modifier
                    .clickable(
                        enabled = isChangeEnabled,
                        role = Role.Button,
                        onClick = onChangeClick,
                    ).padding(horizontal = 12.dp, vertical = 12.dp),
            color = MaterialTheme.colorScheme.primary,
            style =
                TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
        )
    }
}
