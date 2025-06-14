package ru.andreewkov.weightdrop.ui.screen.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.settings.GetSettingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingUseCase
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getWeightingUseCase: GetWeightingUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    private fun initData() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleError()
        }
        viewModelScope.launch(exceptionHandler + Dispatchers.Default) {
            val weightingsDeferred = async { getWeightingUseCase() }
            val settingsDeferred = async { getSettingsUseCase() }
            combine(
                weightingsDeferred.await().getOrThrow(),
                settingsDeferred.await().getOrThrow(),
            ) { weightings, settings ->
                handleCombine(weightings, settings)
            }.collect()
        }
    }

    private fun handleCombine(weightings: List<Weighting>, settings: Settings) {
        val target = settings.targetWeight
        _screenState.value = if (weightings.isEmpty()) {
            ScreenState.Empty
        } else {
            ScreenState.Chart(
                weightChart = weightChartCalculator.calculateWeightChart(target, weightings),
            )
        }
    }

    private fun handleError() {
        // TODO
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data object Empty : ScreenState()

        data class Chart(val weightChart: WeightChart) : ScreenState()
    }
}
