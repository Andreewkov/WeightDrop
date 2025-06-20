package ru.andreewkov.weightdrop.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.WeightingRepository
import ru.andreewkov.weightdrop.data.di.DatabaseDispatcherQualifier
import ru.andreewkov.weightdrop.data.model.WeightingDataModel
import ru.andreewkov.weightdrop.data.model.toWeightingDBO
import ru.andreewkov.weightdrop.data.model.toWeightingDataModel
import ru.andreewkov.weightdrop.data.model.toWeightingDataModels
import ru.andreewkov.weightdrop.database.WeightingDao
import ru.andreewkov.weightdrop.utils.api.LoggerProvider
import java.time.LocalDate
import javax.inject.Inject

class WeightingRepositoryImpl @Inject constructor(
    private val weightingDao: WeightingDao,
    @DatabaseDispatcherQualifier private val databaseDispatcher: CoroutineDispatcher,
    loggerProvider: LoggerProvider,
) : WeightingRepository {

    private val logger = loggerProvider.get("SettingsRepositoryImpl")

    override fun getWeightings(): Result<Flow<List<WeightingDataModel>>> {
        return runCatching(logger, errorMessage = "Error at getting weightings") {
            weightingDao.getWeightings().flowOn(databaseDispatcher).map { weightings ->
                weightings.toWeightingDataModels()
            }
        }
    }

    override suspend fun getWeighting(
        date: LocalDate,
    ): Result<WeightingDataModel> = withContext(databaseDispatcher) {
        return@withContext runCatching(logger, errorMessage = "Error at getting weighting $date") {
            weightingDao.getWeighting(date).toWeightingDataModel()
        }
    }

    override suspend fun updateWeighting(
        weighting: WeightingDataModel,
    ): Result<Unit> = withContext(databaseDispatcher) {
        runCatching(logger, errorMessage = "Error at updating weightings") {
            weightingDao.insertWeighting(
                dbo = weighting.toWeightingDBO(),
            )
        }
    }

    override suspend fun deleteWeighting(
        weighting: WeightingDataModel,
    ): Result<Unit> = withContext(databaseDispatcher) {
        return@withContext runCatching(logger, errorMessage = "Error at deleting weighting") {
            weightingDao.deleteWeighting(
                dbo = weighting.toWeightingDBO(),
            )
        }
    }
}
