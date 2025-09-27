package ru.andreewkov.weightdrop.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.andreewkov.weightdrop.data.api.SettingsRepository
import ru.andreewkov.weightdrop.data.di.SettingsPreferencesQualifier
import ru.andreewkov.weightdrop.data.model.SettingsDataModel
import ru.andreewkov.weightdrop.data.util.asSuccess
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

private const val HEIGHT_NAME = "height"
private const val TARGET_WEIGHT_NAME = "target_weight"
private const val START_WEIGHT_NAME = "start_weight"
private const val START_DATE_NAME = "start_date"

class SettingsRepositoryImpl @Inject constructor(
    @SettingsPreferencesQualifier private val settingsPreferences: SharedPreferences,
) : SettingsRepository {

    private val currentSettings = MutableStateFlow(
        settingsPreferences.run {
            SettingsDataModel(
                height = getInt(HEIGHT_NAME),
                startWeight = getFloat(START_WEIGHT_NAME),
                targetWeight = getFloat(TARGET_WEIGHT_NAME),
                startDate = getLocalDate(START_DATE_NAME),
            )
        }
    )

    override fun observeSettings(): Result<Flow<SettingsDataModel>> {
        return currentSettings.asStateFlow().asSuccess()
    }

    override fun updateHeight(
        height: Int,
    ): Result<Unit> {
        return settingsPreferences.updateValue {
            putInt(HEIGHT_NAME, height)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(height = height)
            }
        }
    }

    override fun updateStartWeight(
        weight: Float,
    ): Result<Unit> {
        return settingsPreferences.updateValue {
            putFloat(START_WEIGHT_NAME, weight)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(startWeight = weight)
            }
        }
    }

    override fun updateTargetWeight(
        weight: Float,
    ): Result<Unit> {
        return settingsPreferences.updateValue {
            putFloat(TARGET_WEIGHT_NAME, weight)
        }.onSuccess {
            currentSettings.update { settings ->
                settings.copy(targetWeight = weight)
            }
        }
    }

    override fun updateStartDate(date: LocalDate): Result<Unit> {
        val mills = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return settingsPreferences.updateValue {
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

    private fun SharedPreferences.getInt(name: String): Int? {
        val value = getInt(name, -1)
        return if (value == -1) {
            null
        } else {
            value
        }
    }

    private fun SharedPreferences.getFloat(name: String): Float? {
        val value = getFloat(name, -1F)
        return if (value == -1F) {
            null
        } else {
            value
        }
    }

    private fun SharedPreferences.getLocalDate(name: String): LocalDate? {
        val value = getLong(START_DATE_NAME, -1L)
        return if (value == -1L) {
            null
        } else {
            Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}
