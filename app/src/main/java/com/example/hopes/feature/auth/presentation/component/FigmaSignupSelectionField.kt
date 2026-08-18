package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

            // Text 유니코드 화살표는 폰트 메트릭 때문에 세로 중앙에서 어긋나 보여
            // 실제 크기가 고정된 Icon으로 대체해 세로 중앙 정렬을 보장한다.
            SelectionFieldDropdownIcon(
                tint = extendedColors.authFieldHint,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 13.dp),
            )
        }
    }
}

/** 선택 필드의 드롭다운 화살표를 필드 높이 기준 세로 중앙에 고정 배치한다. */
@Composable
private fun SelectionFieldDropdownIcon(
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = tint,
        )
    }
}
