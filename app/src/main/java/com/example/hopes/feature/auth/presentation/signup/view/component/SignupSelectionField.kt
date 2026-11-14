package com.example.hopes.feature.auth.presentation.signup.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hopes.core.designsystem.AppBorderWidth
import com.example.hopes.core.designsystem.AppIconSize
import com.example.hopes.core.designsystem.AppRadius
import com.example.hopes.core.designsystem.AppSize
import com.example.hopes.core.designsystem.AppSpacing
import com.example.hopes.core.designsystem.component.shapeClickable
import com.example.hopes.ui.theme.HopesTextStyles
import com.example.hopes.ui.theme.LocalHopesExtendedColors

/**
 * 과·기수처럼 누르면 선택 시트가 열리는 드롭다운 모양의 필드다.
 *
 * @param value 선택된 항목의 표시 문구다. null이면 [placeholder]를 회색으로 보여준다.
 */
@Composable
fun SignupSelectionField(
    value: String?,
    placeholder: String,
    label: String,
    errorMessage: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val extendedColors = LocalHopesExtendedColors.current
    val fieldShape = RoundedCornerShape(AppRadius.Field)
    val fieldDescription = listOfNotNull(label, value).joinToString(separator = ", ")

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(AppSize.Field)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = fieldShape,
                ).border(
                    width =
                        if (errorMessage != null) {
                            AppBorderWidth.Emphasized
                        } else {
                            AppBorderWidth.Default
                        },
                    color =
                        if (errorMessage != null) {
                            MaterialTheme.colorScheme.error
                        } else {
                            extendedColors.authFieldBorder
                        },
                    shape = fieldShape,
                ).shapeClickable(
                    shape = fieldShape,
                    role = Role.DropdownList,
                    onClick = onClick,
                ).semantics {
                    contentDescription = fieldDescription
                    if (errorMessage != null) {
                        error(errorMessage)
                    }
                }.padding(start = AppSpacing.Medium, end = AppSpacing.Item),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = value ?: placeholder,
            modifier = Modifier.weight(1f),
            color =
                if (value != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    extendedColors.authFieldHint
                },
            style = HopesTextStyles.Field,
        )

        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = extendedColors.authFieldHint,
            modifier = Modifier.size(AppIconSize.Medium),
        )
    }
}
