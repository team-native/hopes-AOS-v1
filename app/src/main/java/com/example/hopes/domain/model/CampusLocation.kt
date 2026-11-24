package com.example.hopes.domain.model

data class CampusLocationCheckRequest(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracyMeters: Double,
    val measuredAt: String,
    val clientRequestId: String,
)

data class CampusLocationCheckResult(
    val status: String,
    val accuracyMeters: Double,
    val message: String,
    val floorPlansAvailable: Boolean,
    val matchedBuildingId: Long?,
)
