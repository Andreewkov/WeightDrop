package ru.andreewkov.weightdrop.route.screen.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.WeightingFormatter
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.settings.GetSettingsUseCase
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
import ru.andreewkov.weightdrop.route.base.ObservingFlowProvider
import ru.andreewkov.weightdrop.route.base.ObservingViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateHeightUseCase: UpdateHeightUseCase,
    private val updateStartWeightUseCase: UpdateStartWeightUseCase,
    private val updateTargetWeightUseCase: UpdateTargetWeightUseCase,
    private val updateStartDateUseCase: UpdateStartDateUseCase,
) : ObservingViewModel<SettingsScreenState, Settings>(
    defaultState = SettingsScreenState(content = SettingsContentState.Loading),
    flowProvider = FlowProvider(observeSettingsUseCase),
) {

    fun onSettingsTypeClick(type: SettingsItemType) {
        val currentSettings = getSettingsUseCase()
        val value = when (type) {
            SettingsItemType.Height -> {
                val height = currentSettings.height ?: 0
                HeightValue(value = height)
            }
            SettingsItemType.StartWeight -> {
                val startWeight = currentSettings.startWeight ?: currentSettings.targetWeight ?: 0f
                StartWeightValue(value = startWeight)
            }
            SettingsItemType.TargetWeight -> {
                val targetWeight = currentSettings.targetWeight ?: currentSettings.startWeight ?: 0f
                TargetWeightValue(value = targetWeight)
            }
            SettingsItemType.StartDate -> {
                val startDate = currentSettings.startDate ?: LocalDate.now()
                StartDateValue(value = startDate)
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

    override suspend fun handleObserved(value: Settings) {
        val state = SettingsContentState.Success(
            items = listOf(
                SettingItem(
                    type = SettingsItemType.Height,
                    titleRes = R.string.settings_height_title,
                    text = value.height?.toString(),
                    iconRes = R.drawable.ic_man,
                    textPostfixRes = R.string.height_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.StartWeight,
                    titleRes = R.string.settings_start_weight_title,
                    text = value.startWeight?.let { WeightingFormatter.formatWeightLong(it) },
                    iconRes = R.drawable.ic_floor_scales,
                    textPostfixRes = R.string.weight_wheel_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.TargetWeight,
                    titleRes = R.string.settings_target_weight_title,
                    text = value.targetWeight?.let { WeightingFormatter.formatWeightLong(it) },
                    iconRes = R.drawable.ic_weight_scales,
                    textPostfixRes = R.string.weight_wheel_picker_widget_units,
                ),
                SettingItem(
                    type = SettingsItemType.StartDate,
                    titleRes = R.string.settings_start_date_title,
                    text = value.startDate?.let { WeightingFormatter.formatDateLong(it) },
                    iconRes = R.drawable.ic_calendar,
                ),
            ),
        )
        updateState { copy(content = state) }
    }

    override fun onFailureObserved(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    private class FlowProvider(
        private val observeSettingsUseCase: ObserveSettingsUseCase,
    ) : ObservingFlowProvider<Settings> {
        override fun provideObservingFlow(): Flow<Settings> {
            return observeSettingsUseCase()
        }
    }
}
