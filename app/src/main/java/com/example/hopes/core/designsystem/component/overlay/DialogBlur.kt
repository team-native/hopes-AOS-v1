package com.example.hopes.core.designsystem.component.overlay

import android.os.Build
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogWindowProvider
import kotlin.math.roundToInt

/** 다이얼로그가 표시된 동안 시스템 창 배경에 블러를 적용하고 종료 시 원래 상태로 복원한다. */
@Composable
fun ApplyDialogWindowBackgroundBlur(blurRadius: Dp) {
    val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
    val blurRadiusPx = with(LocalDensity.current) { blurRadius.toPx().roundToInt() }

    DisposableEffect(dialogWindow, blurRadiusPx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dialogWindow?.apply {
                addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                attributes = attributes.apply { blurBehindRadius = blurRadiusPx }
            }
        }

        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dialogWindow?.apply {
                    clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                    attributes = attributes.apply { blurBehindRadius = 0 }
                }
            }
        }
    }
}

/** 시스템 창 블러를 지원하지 않는 기기에서도 동일한 시각 효과를 제공한다. */
fun Modifier.dialogBackdropBlur(
    isEnabled: Boolean,
    blurRadius: Dp,
): Modifier = if (isEnabled) {
    blur(blurRadius)
} else {
    this
}
