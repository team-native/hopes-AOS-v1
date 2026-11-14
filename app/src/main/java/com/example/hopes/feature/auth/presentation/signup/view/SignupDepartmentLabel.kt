package com.example.hopes.feature.auth.presentation.signup.view

import androidx.annotation.StringRes
import com.example.hopes.feature.auth.presentation.signup.viewmodel.SignupDepartment
import com.teamnative.hopes.R

/** 학과 코드를 화면에 표시할 문구 리소스로 바꾼다. */
@StringRes
fun SignupDepartment.labelResId(): Int =
    when (this) {
        SignupDepartment.Software -> R.string.signup_department_software
        SignupDepartment.Iot -> R.string.signup_department_iot
        SignupDepartment.ArtificialIntelligence -> R.string.signup_department_artificial_intelligence
    }
