package ru.andreewkov.weightdrop.data.impl

import kotlinx.coroutines.flow.Flow
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.model.SettingsDataModel

class SettingsRepositoryImpl : SettingsRepository {

    override fun getSettings(): Flow<SettingsDataModel> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTargetWeight(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateHeight(): Result<Unit> {
        TODO("Not yet implemented")
    }
}