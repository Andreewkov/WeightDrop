package ru.andreewkov.weightdrop.data.model

import java.time.LocalDate

data class SettingsDataModel(
    val isLoading: Boolean,
    val height: Int? = null,
    val startWeight: Float? = null,
    val targetWeight: Float? = null,
    val startDate: LocalDate? = null,
)
