package ru.andreewkov.weightdrop.data.model

import java.time.LocalDate

data class SettingsDataModel(
    val height: Int?,
    val startWeight: Float?,
    val targetWeight: Float?,
    val startDate: LocalDate?,
)
