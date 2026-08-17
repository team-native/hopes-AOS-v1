package com.example.hopes.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.hopes.R

/** 설정 화면과 상세 화면에서 동일하게 사용하는 피그마 뒤로가기 버튼이다. */
@Composable
fun FigmaBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val backDescription = stringResource(R.string.back)

    Box(
        modifier = modifier
            .size(38.dp)
            .figmaSubtleShadow(RoundedCornerShape(13.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(13.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(13.dp))
            .semantics {
                role = Role.Button
                contentDescription = backDescription
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Default.ChevronLeft,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}
