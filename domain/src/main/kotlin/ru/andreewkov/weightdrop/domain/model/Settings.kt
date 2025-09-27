package ru.andreewkov.weightdrop.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import java.time.LocalDate

data class Settings(
    val height: Int?,
    val startWeight: Float?,
    val targetWeight: Float?,
    val startDate: LocalDate?,
)

internal fun SettingsDataModel.toSettings(): Settings {
    return Settings(height, startWeight, targetWeight, startDate)
}

internal fun Flow<SettingsDataModel>.toSettingsFlow(): Flow<Settings> {
    return map { it.toSettings() }
}
