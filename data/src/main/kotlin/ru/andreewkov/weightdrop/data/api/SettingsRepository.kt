package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import java.time.LocalDate

interface SettingsRepository {

    fun observeSettings(): Result<Flow<SettingsDataModel>>

    fun updateHeight(height: Int): Result<Unit>

    fun updateStartWeight(weight: Float): Result<Unit>

    fun updateTargetWeight(weight: Float): Result<Unit>

    fun updateStartDate(date: LocalDate): Result<Unit>
}
