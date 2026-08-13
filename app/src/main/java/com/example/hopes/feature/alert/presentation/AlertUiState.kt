package com.example.hopes.feature.alert.presentation

import androidx.annotation.StringRes
import com.example.hopes.R

/** 피그마 알림 행에 필요한 표시 데이터를 모델링한다. */
data class AlertNotificationUiModel(
    val id: String,
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int,
    @param:StringRes val actionLabelRes: Int,
)

/** 알림 화면의 로컬 데모 상태를 제공한다. */
data class AlertUiState(
    val isReadAll: Boolean = false,
    val isEditing: Boolean = false,
    val notifications: List<AlertNotificationUiModel> = defaultAlertNotifications,
)

private val defaultAlertNotifications = listOf(
    AlertNotificationUiModel(
        id = "new-answer",
        titleRes = R.string.alert_answer_arrived_title,
        descriptionRes = R.string.alert_answer_arrived_description,
        actionLabelRes = R.string.view,
    ),
    AlertNotificationUiModel(
        id = "profile-saved",
        titleRes = R.string.alert_profile_saved_title,
        descriptionRes = R.string.alert_profile_saved_description,
        actionLabelRes = R.string.confirm,
    ),
    AlertNotificationUiModel(
        id = "admission-guide",
        titleRes = R.string.alert_admission_title,
        descriptionRes = R.string.alert_admission_description,
        actionLabelRes = R.string.open,
    ),
    AlertNotificationUiModel(
        id = "system-notice",
        titleRes = R.string.alert_system_title,
        descriptionRes = R.string.alert_system_description,
        actionLabelRes = R.string.view,
    ),
)
