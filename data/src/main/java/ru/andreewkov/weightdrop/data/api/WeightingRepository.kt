package ru.andreewkov.weightdrop.data.api

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.model.Weighting

interface WeightingRepository {

    fun getWeightings(): Result<Flow<List<Weighting>>>

    suspend fun updateWeighting(weighting: Weighting): Result<Unit>
}