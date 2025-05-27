package ru.andreewkov.weightdrop.data.impl

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import ru.andreewkov.weightdrop.database.WeightingDao
import ru.andreewkov.weightdrop.database.WeightingDBO
import javax.inject.Inject

class WeightingRepositoryImpl @Inject constructor(
    private val weightingDao: WeightingDao,
) : WeightingRepository {

    override fun getWeightings(): Result<Flow<List<WeightingDataModel>>> {
        return Result.success(
            weightingDao.getWeightings().map { it.map { WeightingDataModel(it.weight, it.date) } }
        )
    }

    override suspend fun updateWeighting(weighting: WeightingDataModel): Result<Unit> = coroutineScope {
        weightingDao.insertWeighting(
            WeightingDBO(
                date = weighting.date,
                weight = weighting.value,
            )
        )
        return@coroutineScope Result.success(Unit)
    }
}