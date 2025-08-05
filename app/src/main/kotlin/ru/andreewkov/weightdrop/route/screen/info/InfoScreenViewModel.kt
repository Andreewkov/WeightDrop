package ru.andreewkov.weightdrop.route.screen.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.WeightChartCalculator
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.settings.GetSettingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingsUseCase
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(
    private val getWeightingsUseCase: GetWeightingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
) : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<InfoScreenState>(InfoScreenState.Loading)
    val screenState get() = _screenState.asStateFlow()

    init {
        initData()
    }

    private fun initData() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleError()
        }
        viewModelScope.launch(exceptionHandler + Dispatchers.Default) {
            val weightingsDeferred = async { getWeightingsUseCase() }
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
        val value = if (weightings.isEmpty()) {
            InfoScreenState.Empty
        } else {
            InfoScreenState.Chart(
                weightChart = weightChartCalculator.calculateWeightChart(target, weightings),
            )
        }
        _screenState.value = value
    }

    private fun handleError() {
        // TODO
    }
}
