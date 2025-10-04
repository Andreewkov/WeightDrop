package ru.andreewkov.weightdrop.domain.model

import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import java.time.LocalDate

data class Settings(
    val isLoading: Boolean,
    val height: Int?,
    val startWeight: Float?,
    val targetWeight: Float?,
    val startDate: LocalDate?,
)

internal fun SettingsDataModel.toSettings(): Settings {
    return Settings(
        isLoading = isLoading,
        height = height,
        startWeight = startWeight,
        targetWeight = targetWeight,
        startDate = startDate,
    )
}
