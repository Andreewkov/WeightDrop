package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

interface SettingsRepository {

    fun observeSettings(): Result<Flow<SettingsDataModel>>

    suspend fun updateHeight(height: Int): Result<Unit>

    suspend fun updateStartWeight(weight: Float): Result<Unit>

    suspend fun updateTargetWeight(weight: Float): Result<Unit>
}
