package com.example.hopes.core.designsystem.component.overlay

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import com.example.hopes.core.designsystem.AppBlurRadius

/** 오버레이 뒤에 유지되는 화면 전체 배경에 공통 blur 효과를 적용한다. */
fun Modifier.overlayBackdropBlur(): Modifier = blur(AppBlurRadius.OverlayBackdrop)
