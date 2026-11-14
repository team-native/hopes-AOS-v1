package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.ui.theme.HopesTextStyles

/**
 * 체크(✓) 또는 엑스(✕) 아이콘이 붙은 한 줄 문구다. 비밀번호 조건 목록과 "이메일 인증이 완료됐어요" 문구가 쓴다.
 *
 * @param accessibilityText 색만으로 구분되는 충족 여부를 화면 낭독기에 전달할 문구다.
 */
@Composable
fun SignupCheckMessage(
    text: String,
    isChecked: Boolean,
    color: Color,
    accessibilityText: String,
    modifier: Modifier = Modifier,
    isEmphasized: Boolean = false,
) {
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = accessibilityText },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector =
                if (isChecked) {
                    Icons.Filled.Check
                } else {
                    Icons.Filled.Close
                },
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(AppIconSize.Inline),
        )

        Spacer(modifier = Modifier.width(AppSpacing.Small))

        Text(
            text = text,
            color = color,
            style =
                HopesTextStyles.Caption.copy(
                    fontWeight =
                        if (isEmphasized) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                ),
        )
    }
}
