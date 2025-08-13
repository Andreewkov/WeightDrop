package ru.andreewkov.weightdrop.data.impl

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsDispatcherQualifier
import ru.andreewkov.weightdrop.data.di.SettingsPreferencesQualifier
import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import ru.andreewkov.weightdrop.utils.api.LoggerProvider
import javax.inject.Inject

private const val HEIGHT_NAME = "target_weight"
private const val TARGET_WEIGHT_NAME = "target_weight"
private const val START_WEIGHT_NAME = "start_weight"

class SettingsRepositoryImpl @Inject constructor(
    @SettingsPreferencesQualifier private val settingsPreferences: SharedPreferences,
    @SettingsDispatcherQualifier private val settingsDispatcher: CoroutineDispatcher,
    loggerProvider: LoggerProvider,
) : SettingsRepository {

    private val logger = loggerProvider.get("SettingsRepositoryImpl")

    private val currentSettings = MutableStateFlow(
        SettingsDataModel(
            height = getHeight(),
            startWeight = getStartWeight(),
            targetWeight = getTargetWeight(),
        ),
    )

    override fun observeSettings(): Result<Flow<SettingsDataModel>> {
        return currentSettings.asStateFlow().asSuccess()
    }

    override suspend fun updateHeight(
        height: Int,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating height") {
            settingsPreferences.edit().apply {
                putInt(HEIGHT_NAME, height)
            }
            currentSettings.value = currentSettings.value.copy(height = height)
        }
    }

    override suspend fun updateStartWeight(
        weight: Float,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit().apply {
                putFloat(START_WEIGHT_NAME, weight)
            }.apply()
            currentSettings.value = currentSettings.value.copy(startWeight = weight)
        }
    }

    override suspend fun updateTargetWeight(
        weight: Float,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit().apply {
                putFloat(TARGET_WEIGHT_NAME, weight)
            }.apply()
            currentSettings.value = currentSettings.value.copy(targetWeight = weight)
        }
    }

    private fun getHeight(): Int? {
        val value = settingsPreferences.getInt(HEIGHT_NAME, -1)
        return if (value == -1) {
            null
        } else {
            value
        }
    }

    private fun getStartWeight(): Float? {
        val value = settingsPreferences.getFloat(START_WEIGHT_NAME, -1f)
        return if (value == -1f) {
            null
        } else {
            value
        }
    }

    private fun getTargetWeight(): Float? {
        val value = settingsPreferences.getFloat(TARGET_WEIGHT_NAME, -1f)
        return if (value == -1f) {
            null
        } else {
            value
        }
    }
}
