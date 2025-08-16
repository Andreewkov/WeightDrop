package ru.andreewkov.weightdrop.route.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.settings.ObserveSettingsUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateHeightUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateStartWeightUseCase
import ru.andreewkov.weightdrop.domain.settings.UpdateTargetWeightUseCase
import ru.andreewkov.weightdrop.model.SettingItem
import ru.andreewkov.weightdrop.model.SettingsItemType
import ru.andreewkov.weightdrop.model.SettingsUpdateValue
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val updateHeightUseCase: UpdateHeightUseCase,
    private val updateStartWeightUseCase: UpdateStartWeightUseCase,
    private val updateTargetWeightUseCase: UpdateTargetWeightUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<SettingsScreenState>(SettingsScreenState.Loading)
    val screenState get() = _screenState.asStateFlow()

    init {
        loadData()
    }

    fun updateSettingsValue(settingsValue: SettingsUpdateValue) {
        viewModelScope.launch {
            runCatching {
                when (settingsValue) {
                    is SettingsUpdateValue.HeightValue -> updateHeightUseCase(settingsValue.value)
                    is SettingsUpdateValue.StartWeightValue -> updateStartWeightUseCase(settingsValue.value)
                    is SettingsUpdateValue.TargetWeightValue -> updateTargetWeightUseCase(settingsValue.value)
                }
            }.onFailure {
                _screenState.update { SettingsScreenState.Failure }
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
                _screenState.update { SettingsScreenState.Failure }
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
                    text = height?.toString() ?: "-",
                    iconRes = R.drawable.ic_floor_scales,
                ),
                SettingItem(
                    type = SettingsItemType.TargetWeight,
                    titleRes = R.string.settings_target_weight_title,
                    text = height?.toString() ?: "-",
                    iconRes = R.drawable.ic_weight_scales,
                ),
            ),
        )
        _screenState.update { state }
    }
}
