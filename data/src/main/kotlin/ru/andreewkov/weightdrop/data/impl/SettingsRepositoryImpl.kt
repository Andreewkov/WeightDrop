package ru.andreewkov.weightdrop.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsDispatcherQualifier
import ru.andreewkov.weightdrop.data.di.SettingsPreferencesQualifier
import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

private const val HEIGHT_NAME = "height"
private const val TARGET_WEIGHT_NAME = "target_weight"
private const val START_WEIGHT_NAME = "start_weight"
private const val START_DATE_NAME = "start_date"

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @SettingsPreferencesQualifier private val settingsPreferences: SharedPreferences,
    @SettingsDispatcherQualifier private val dispatcher: CoroutineDispatcher,
) : SettingsRepository {

    private val scope = CoroutineScope(dispatcher)

    private val currentSettings = MutableStateFlow(
        SettingsDataModel(isLoading = true),
    )

    init {
        scope.launch {
            currentSettings.update {
                settingsPreferences.run {
                    SettingsDataModel(
                        isLoading = false,
                        height = getInt(HEIGHT_NAME),
                        startWeight = getFloat(START_WEIGHT_NAME),
                        targetWeight = getFloat(TARGET_WEIGHT_NAME),
                        startDate = getLocalDate(START_DATE_NAME),
                    )
                }
            }
        }
    }

    override fun observeSettings(): Flow<SettingsDataModel> {
        return currentSettings.asStateFlow()
    }

    override suspend fun updateHeight(
        height: Int,
    ): Result<Unit> = withContext(dispatcher) {
        settingsPreferences.updateValue {
            putInt(HEIGHT_NAME, height)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(height = height)
            }
        }
    }

    override suspend fun updateStartWeight(
        weight: Float,
    ): Result<Unit> = withContext(dispatcher) {
        settingsPreferences.updateValue {
            putFloat(START_WEIGHT_NAME, weight)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(startWeight = weight)
            }
        }
    }

    override suspend fun updateTargetWeight(
        weight: Float,
    ): Result<Unit> = withContext(dispatcher) {
        settingsPreferences.updateValue {
            putFloat(TARGET_WEIGHT_NAME, weight)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(targetWeight = weight)
            }
        }
    }

    override suspend fun updateStartDate(
        date: LocalDate,
    ): Result<Unit> = withContext(dispatcher) {
        val mills = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        settingsPreferences.updateValue {
            putLong(START_DATE_NAME, mills)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(startDate = date)
            }
        }
    }

    private fun SharedPreferences.updateValue(
        action: SharedPreferences.Editor.() -> Unit,
    ): Result<Unit> {
        return runCatching {
            settingsPreferences.edit(action = action)
        }.onFailure { throwable ->
            println(throwable)
        }
    }

    private suspend fun SharedPreferences.getInt(name: String): Int? = withContext(dispatcher) {
        val value = getInt(name, -1)
        if (value == -1) {
            null
        } else {
            value
        }
    }

    private suspend fun SharedPreferences.getFloat(name: String): Float? = withContext(dispatcher) {
        val value = getFloat(name, -1F)
        if (value == -1F) {
            null
        } else {
            value
        }
    }

    private suspend fun SharedPreferences.getLocalDate(name: String): LocalDate? = withContext(dispatcher) {
        val value = getLong(name, -1L)
        if (value == -1L) {
            null
        } else {
            Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}
