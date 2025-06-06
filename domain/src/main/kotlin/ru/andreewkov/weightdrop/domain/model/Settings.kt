package ru.andreewkov.weightdrop.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

data class Settings(
    val targetWeight: Float?,
    val height: Int?,
)

internal fun SettingsDataModel.toSettings(): Settings {
    return Settings(targetWeight, height)
}

internal fun Flow<SettingsDataModel>.toSettingsFlow(): Flow<Settings> {
    return map { it.toSettings() }
}
