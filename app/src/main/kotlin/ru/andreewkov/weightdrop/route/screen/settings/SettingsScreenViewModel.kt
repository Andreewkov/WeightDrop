package ru.andreewkov.weightdrop.route.screen.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.settings.ObserveSettingsUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateHeightUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateStartDateUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateStartWeightUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateTargetWeightUseCase
import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsItemType
import ru.andreewkov.weightdrop.model.SettingsUpdateValue
import ru.andreewkov.weightdrop.route.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateHeightUseCase: UpdateHeightUseCase,
    private val updateStartWeightUseCase: UpdateStartWeightUseCase,
    private val updateTargetWeightUseCase: UpdateTargetWeightUseCase,
    private val updateStartDateUseCase: UpdateStartDateUseCase,
) : BaseViewModel<SettingsScreenState>(
    defaultState = SettingsScreenState.Loading,
) {

    init {
        loadData()
    }

    fun updateSettingsValue(settingsValue: SettingsUpdateValue<*>) {
        viewModelScope.launch {
            runCatching {
                when (settingsValue) {
                    is SettingsUpdateValue.HeightValue -> updateHeightUseCase(settingsValue.value)
                    is SettingsUpdateValue.StartWeightValue -> updateStartWeightUseCase(settingsValue.value)
                    is SettingsUpdateValue.TargetWeightValue -> updateTargetWeightUseCase(settingsValue.value)
                    is SettingsUpdateValue.StartDateValue -> updateStartDateUseCase(settingsValue.value)
                }
            }.onFailure {
                updateState { SettingsScreenState.Failure }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching {
                val settingsFlow = observeSettingsUseCase()
                settingsFlow.getOrThrow().collect { settings ->
                    settings.handle()
                }
            }.onFailure {
                updateState { SettingsScreenState.Failure }
            }
        }
    }

    private fun Settings.handle() {
        val state = SettingsScreenState.Success(
            items = listOf(
                SettingItem(
                    type = SettingsItemType.Height,
                    titleRes = R.string.settings_height_title,
                    text = height?.toString() ?: "-",
                    iconRes = R.drawable.ic_man,
                ),
                SettingItem(
                    type = SettingsItemType.StartWeight,
                    titleRes = R.string.settings_start_weight_title,
                    text = startWeight?.toString() ?: "-",
                    iconRes = R.drawable.ic_floor_scales,
                ),
                SettingItem(
                    type = SettingsItemType.TargetWeight,
                    titleRes = R.string.settings_target_weight_title,
                    text = targetWeight?.toString() ?: "-",
                    iconRes = R.drawable.ic_weight_scales,
                ),
                SettingItem(
                    type = SettingsItemType.StartDate,
                    titleRes = R.string.settings_start_date_title,
                    text = startDate?.let { WeightingFormatter.formatDateLong(it) } ?: "-",
                    iconRes = R.drawable.ic_calendar,
                ),
            ),
        )
        updateState { state }
    }
}
