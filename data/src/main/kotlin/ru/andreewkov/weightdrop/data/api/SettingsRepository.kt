package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

interface SettingsRepository {

    fun getSettings(): Result<Flow<SettingsDataModel>>

    suspend fun updateTargetWeight(weight: Float): Result<Unit>

    suspend fun updateHeight(height: Int): Result<Unit>
}
