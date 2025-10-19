package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import java.time.LocalDate

interface WeightingRepository {

    fun observeWeightings(): Flow<List<WeightingDataModel>>

    suspend fun getWeighting(date: LocalDate): Result<WeightingDataModel>

    suspend fun updateWeighting(weighting: WeightingDataModel): Result<Unit>

    suspend fun deleteWeighting(weighting: WeightingDataModel): Result<Unit>
}
