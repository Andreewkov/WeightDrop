package ru.andreewkov.weightdrop.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.data.di.DatabaseDispatcherQualifier
import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import ru.andreewkov.weightdrop.data.model.toDataModel
import ru.andreewkov.weightdrop.data.model.toDataModels
import ru.andreewkov.weightdrop.data.model.toEntity
import ru.andreewkov.weightdrop.database.WeightingDao
import java.time.LocalDate
import javax.inject.Inject

class WeightingRepositoryImpl @Inject constructor(
    private val weightingDao: WeightingDao,
    @DatabaseDispatcherQualifier private val databaseDispatcher: CoroutineDispatcher,
) : WeightingRepository {

    override fun observeWeightings(): Flow<List<WeightingDataModel>> {
        return weightingDao.observeWeightings()
            .map { weightings ->
                weightings.toDataModels()
            }
            .flowOn(databaseDispatcher)
    }

    override suspend fun getWeighting(
        date: LocalDate,
    ): Result<WeightingDataModel> = withContext(databaseDispatcher) {
        runCatching {
            weightingDao.getWeighting(date).toDataModel()
        }
    }

    override suspend fun updateWeighting(
        weighting: WeightingDataModel,
    ): Result<Unit> = withContext(databaseDispatcher) {
        runCatching {
            weightingDao.insertWeighting(
                entity = weighting.toEntity(),
            )
        }
    }

    override suspend fun deleteWeighting(
        weighting: WeightingDataModel,
    ): Result<Unit> = withContext(databaseDispatcher) {
        runCatching {
            weightingDao.deleteWeighting(
                entity = weighting.toEntity(),
            )
        }
    }
}
