package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

interface SettingsRepository {

    fun getSettings(): Flow<SettingsDataModel>

    suspend fun updateTargetWeight(): Result<Unit>

    suspend fun updateHeight(): Result<Unit>
}