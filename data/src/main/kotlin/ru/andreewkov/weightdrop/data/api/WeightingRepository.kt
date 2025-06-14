package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.WeightingDataModel

interface WeightingRepository {

    fun getWeightings(): Result<Flow<List<WeightingDataModel>>>

    suspend fun updateWeighting(weighting: WeightingDataModel): Result<Unit>

    suspend fun deleteWeighting(weighting: WeightingDataModel): Result<Unit>
}
