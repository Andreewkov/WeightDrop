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
import androidx.core.content.edit
import java.sql.Date
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private const val HEIGHT_NAME = "height"
private const val TARGET_WEIGHT_NAME = "target_weight"
private const val START_WEIGHT_NAME = "start_weight"
private const val START_DATE_NAME = "start_date"

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
            startDate = getStartDate(),
        ),
    )

    override fun observeSettings(): Result<Flow<SettingsDataModel>> {
        return currentSettings.asStateFlow().asSuccess()
    }

    override suspend fun updateHeight(
        height: Int,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating height") {
            settingsPreferences.edit {
                putInt(HEIGHT_NAME, height)
            }
            currentSettings.value = currentSettings.value.copy(height = height)
        }
    }

    override suspend fun updateStartWeight(
        weight: Float,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit {
                putFloat(START_WEIGHT_NAME, weight)
            }
            currentSettings.value = currentSettings.value.copy(startWeight = weight)
        }
    }

    override suspend fun updateTargetWeight(
        weight: Float,
    ): Result<Unit> = withContext(settingsDispatcher) {
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit {
                putFloat(TARGET_WEIGHT_NAME, weight)
            }
            currentSettings.value = currentSettings.value.copy(targetWeight = weight)
        }
    }

    override suspend fun updateStartDate(
        date: LocalDate,
    ): Result<Unit> = withContext(settingsDispatcher) {
        val mills = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return@withContext runCatching(logger, "Exception at updating target weight") {
            settingsPreferences.edit {
                putLong(START_DATE_NAME, mills)
            }
            currentSettings.value = currentSettings.value.copy(startDate = date)
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

    private fun getStartDate(): LocalDate? {
        val value = settingsPreferences.getLong(START_DATE_NAME, -1L)
        return if (value == -1L) {
            null
        } else {
            Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}
