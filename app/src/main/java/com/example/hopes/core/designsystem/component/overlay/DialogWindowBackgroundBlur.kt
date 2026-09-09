package com.example.hopes.core.designsystem.component.overlay

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogWindowProvider
import kotlin.math.roundToInt

/**
 * Dialog/ModalBottomSheet 뒤에 보이는 실제 화면에 Android 네이티브 Window 배경 블러를 적용한다.
 * minSdk 34(> API 31)라 별도 버전 분기 없이 [android.view.Window.setBackgroundBlurRadius]를 바로 쓸 수 있다.
 * `Modifier.blur()`는 같은 Window 안의 컴포저블만 흐리게 하므로, Dialog처럼 별도 Window로 뜨는
 * 오버레이 뒤의 실제 화면을 흐리게 하려면 이 Window API가 필요하다.
 */
@Composable
fun ApplyDialogWindowBackgroundBlur(blurRadius: Dp) {
    val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
    val blurRadiusPx = with(LocalDensity.current) { blurRadius.toPx().roundToInt() }

    DisposableEffect(dialogWindow, blurRadiusPx) {
        dialogWindow?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            attributes = attributes.apply { blurBehindRadius = blurRadiusPx }
        }

        onDispose {
            dialogWindow?.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        }
    }
}
