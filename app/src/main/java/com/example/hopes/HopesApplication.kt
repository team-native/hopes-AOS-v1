package com.example.hopes

import dagger.hilt.android.HiltAndroidApp

/** Hilt 객체 그래프를 초기화하는 애플리케이션 진입점이다. */
@HiltAndroidApp(android.app.Application::class)
class HopesApplication : Hilt_HopesApplication()
