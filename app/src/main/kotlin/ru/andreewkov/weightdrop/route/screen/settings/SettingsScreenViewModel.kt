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
import ru.andreewkov.weightdrop.model.SettingsDialogValue
import ru.andreewkov.weightdrop.model.SettingsDialogValue.HeightValue
import ru.andreewkov.weightdrop.model.SettingsDialogValue.StartDateValue
import ru.andreewkov.weightdrop.model.SettingsDialogValue.StartWeightValue
import ru.andreewkov.weightdrop.model.SettingsDialogValue.TargetWeightValue
import ru.andreewkov.weightdrop.model.SettingsItemType
import ru.andreewkov.weightdrop.route.base.BaseViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateHeightUseCase: UpdateHeightUseCase,
    private val updateStartWeightUseCase: UpdateStartWeightUseCase,
    private val updateTargetWeightUseCase: UpdateTargetWeightUseCase,
    private val updateStartDateUseCase: UpdateStartDateUseCase,
) : BaseViewModel<SettingsScreenState>(
    defaultState = SettingsScreenState(content = SettingsContentState.Loading),
) {

    private var currentSettings: Settings? = null
        set(value) {
            field = value
            value?.handle()
        }

    init {
        loadData()
    }

    fun onSettingsTypeClick(type: SettingsItemType) {
        val value = when (type) {
            SettingsItemType.Height -> {
                HeightValue(currentSettings?.height ?: 0)
            }
            SettingsItemType.StartWeight -> {
                StartWeightValue(currentSettings?.startWeight ?: currentSettings?.targetWeight ?: 0f)
            }
            SettingsItemType.TargetWeight -> {
                TargetWeightValue(currentSettings?.targetWeight ?: currentSettings?.startWeight ?: 0f)
            }
            SettingsItemType.StartDate -> {
                StartDateValue(currentSettings?.startDate ?: LocalDate.now())
            }
        }
        updateState { copy(displayedDialog = value) }
    }

    fun onDialogValueChanged(dialogValue: SettingsDialogValue<*>) {
        viewModelScope.launch {
            val result = when (dialogValue) {
                is HeightValue -> updateHeightUseCase(dialogValue.value)
                is StartWeightValue -> updateStartWeightUseCase(dialogValue.value)
                is TargetWeightValue -> updateTargetWeightUseCase(dialogValue.value)
                is StartDateValue -> updateStartDateUseCase(dialogValue.value)
            }
            if (result.isFailure) {
                updateState { copy(content = SettingsContentState.Failure) }
            }
        }
    }

    fun onDialogDismissRequest() {
        updateState { copy(displayedDialog = null) }
    }

    private fun loadData() {
        viewModelScope.launch {
            runCatching {
                val settingsFlow = observeSettingsUseCase()
                settingsFlow.collect { settings ->
                    currentSettings = settings
                }
            }.onFailure {
                // TODO
            }
        }
    }

    private fun Settings.handle() {
        val state = SettingsContentState.Success(
            items = listOf(
                SettingItem(
                    type = SettingsItemType.Height,
                    titleRes = R.string.settings_height_title,
                    text = height?.toString(),
                    iconRes = R.drawable.ic_man,
                    textPostfixRes = R.string.height_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.StartWeight,
                    titleRes = R.string.settings_start_weight_title,
                    text = startWeight?.let { WeightingFormatter.formatWeightLong(it) },
                    iconRes = R.drawable.ic_floor_scales,
                    textPostfixRes = R.string.weight_wheel_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.TargetWeight,
                    titleRes = R.string.settings_target_weight_title,
                    text = targetWeight?.let { WeightingFormatter.formatWeightLong(it) },
                    iconRes = R.drawable.ic_weight_scales,
                    textPostfixRes = R.string.weight_wheel_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.StartDate,
                    titleRes = R.string.settings_start_date_title,
                    text = startDate?.let { WeightingFormatter.formatDateLong(it) },
                    iconRes = R.drawable.ic_calendar,
                ),
            ),
        )
        updateState { copy(content = state) }
    }
}
