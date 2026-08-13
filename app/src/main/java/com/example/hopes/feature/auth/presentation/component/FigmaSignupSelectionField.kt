package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.R
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/** 회원가입의 학과·기수 선택값을 같은 규격으로 표시하는 필드다. */
@Composable
fun FigmaSignupSelectionField(
    selectedValue: String,
    placeholder: String,
    isError: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 17.dp, end = 18.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(43.dp)
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        extendedColors.authFieldBorder
                    },
                    shape = RoundedCornerShape(14.dp),
                )
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                .clickable(role = Role.Button, onClick = onClick)
                .semantics { contentDescription = placeholder },
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = selectedValue.ifBlank { placeholder },
                modifier = Modifier.padding(start = 12.dp, end = 40.dp),
                color = if (selectedValue.isBlank()) {
                    extendedColors.authFieldHint
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                style = TextStyle(fontSize = 15.sp),
            )

            Text(
                text = stringResource(R.string.signup_dropdown_symbol),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 13.dp),
                color = extendedColors.authFieldHint,
                style = TextStyle(fontSize = 20.sp),
            )
        }
    }
}
