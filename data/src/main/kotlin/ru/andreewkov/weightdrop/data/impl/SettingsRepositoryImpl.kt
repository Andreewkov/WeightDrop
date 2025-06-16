package ru.andreewkov.weightdrop.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
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

private const val TARGET_WEIGHT_NAME = "target_weight"
private const val HEIGHT_NAME = "target_weight"

class SettingsRepositoryImpl @Inject constructor(
    @SettingsPreferencesQualifier private val settingsPreferences: SharedPreferences,
    @SettingsDispatcherQualifier private val settingsDispatcher: CoroutineDispatcher,
    loggerProvider: LoggerProvider,
) : SettingsRepository {

    private val logger = loggerProvider.get("SettingsRepositoryImpl")

    private val _currentSettings = MutableStateFlow(
        SettingsDataModel(
            targetWeight = getTargetWeight(),
            height = getHeight(),
        ),
    )

    override fun getSettings(): Result<Flow<SettingsDataModel>> {
        return _currentSettings.asStateFlow().asSuccess()
    }

    override suspend fun updateTargetWeight(
        weight: Float,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit {
                putFloat(TARGET_WEIGHT_NAME, weight)
            }
            _currentSettings.value = _currentSettings.value.copy(targetWeight = weight)
        }
    }

    override suspend fun updateHeight(
        height: Int,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating height") {
            settingsPreferences.edit {
                putInt(HEIGHT_NAME, height)
            }
            _currentSettings.value = _currentSettings.value.copy(height = height)
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

    private fun getHeight(): Int? {
        val value = settingsPreferences.getInt(HEIGHT_NAME, -1)
        return if (value == -1) {
            null
        } else {
            value
        }
    }
}
