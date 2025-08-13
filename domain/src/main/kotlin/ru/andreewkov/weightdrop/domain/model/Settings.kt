package ru.andreewkov.weightdrop.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

data class Settings(
    val height: Int?,
    val startWeight: Float?,
    val targetWeight: Float?,
)

internal fun SettingsDataModel.toSettings(): Settings {
    return Settings(height, startWeight, targetWeight)
}

internal fun Flow<SettingsDataModel>.toSettingsFlow(): Flow<Settings> {
    return map { it.toSettings() }
}
