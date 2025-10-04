package ru.andreewkov.weightdrop.route.screen.info

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.andreewkov.weightdrop.WeightChartCalculator
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.settings.ObserveSettingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingsUseCase
import ru.andreewkov.weightdrop.route.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(
    private val getWeightingsUseCase: GetWeightingsUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
) : BaseViewModel<InfoScreenState>(
    defaultState = InfoScreenState.Loading,
) {

    private val weightChartCalculator = WeightChartCalculator()

    init {
        initData()
    }

    private fun initData() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleError()
        }
        viewModelScope.launch(exceptionHandler + Dispatchers.Default) {
            val weightingsDeferred = async { getWeightingsUseCase() }
            val settingsDeferred = async { observeSettingsUseCase() }
            combine(
                weightingsDeferred.await().getOrThrow(),
                settingsDeferred.await(),
            ) { weightings, settings ->
                if (settings.isLoading) return@combine

                handleCombine(weightings, settings)
            }.collect()
        }
    }

    private suspend fun handleCombine(weightings: List<Weighting>, settings: Settings) {
        val target = settings.targetWeight
        val state = if (weightings.isEmpty()) {
            InfoScreenState.Empty
        } else {
            withContext(Dispatchers.Default) {
                InfoScreenState.Chart(
                    weightChart = weightChartCalculator.calculateWeightChart(target, weightings),
                )
            }
        }
        updateState { state }
    }

    private fun handleError() {
        // TODO
    }
}
