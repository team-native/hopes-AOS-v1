package com.example.hopes.feature.auth.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hopes.core.designsystem.component.figmaSheetShadow
import com.example.hopes.core.designsystem.component.figmaPeekSheetShadow

/** 피그마 인증 화면의 상단 모서리만 둥근 하단 시트다. */
@Composable
fun FigmaAuthSheet(
    modifier: Modifier = Modifier,
    isPeekSheet: Boolean,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetShape = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isPeekSheet) {
                    Modifier.figmaPeekSheetShadow(sheetShape)
                } else {
                    Modifier.figmaSheetShadow(sheetShape)
                },
            ),
        color = MaterialTheme.colorScheme.surface,
        shape = sheetShape,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            content = content,
        )
    }
}
