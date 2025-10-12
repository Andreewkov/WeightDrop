package ru.andreewkov.weightdrop.route.screen.info

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.settings.ObserveSettingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.CalculateWeightingsChartUseCase
import ru.andreewkov.weightdrop.domain.weighting.ObserveWeightingsUseCase
import ru.andreewkov.weightdrop.route.base.ScreenStateViewModel
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(
    private val observeWeightingsUseCase: ObserveWeightingsUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val calculateWeightingsChartUseCase: CalculateWeightingsChartUseCase,
) : ScreenStateViewModel<InfoScreenState>(
    defaultState = InfoScreenState.Loading,
) {

    init {
        initData()
    }

    private fun initData() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleError()
        }
        viewModelScope.launch(exceptionHandler + Dispatchers.Default) {
            val weightingsDeferred = async { observeWeightingsUseCase() }
            val settingsDeferred = async { observeSettingsUseCase() }
            combine(
                weightingsDeferred.await().getOrThrow(), // TODO
                settingsDeferred.await(),
            ) { weightings, settings ->
                handleCombine(weightings, settings)
            }.collect()
        }
    }

    private suspend fun handleCombine(weightings: List<Weighting>, settings: Settings) {
        val target = settings.targetWeight
        val state = if (weightings.isEmpty()) {
            InfoScreenState.SuccessEmpty
        } else {
            val chartResult = calculateWeightingsChartUseCase(target, weightings).getOrNull()

            chartResult?.let { chart ->
                InfoScreenState.SuccessChart(
                    chart = chartResult,
                )
            } ?: InfoScreenState.Failure
        }

        updateState { state }
    }

    private fun handleError() {
        // TODO
    }
}
