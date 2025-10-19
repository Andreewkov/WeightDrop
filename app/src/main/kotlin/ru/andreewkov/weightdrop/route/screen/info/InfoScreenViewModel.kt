package ru.andreewkov.weightdrop.route.screen.info

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.andreewkov.weightdrop.domain.settings.ObserveSettingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.CalculateWeightingsChartUseCase
import ru.andreewkov.weightdrop.domain.weighting.ObserveWeightingsUseCase
import ru.andreewkov.weightdrop.model.InfoObservingItem
import ru.andreewkov.weightdrop.route.base.ObservingFlowProvider
import ru.andreewkov.weightdrop.route.base.ObservingViewModel
import javax.inject.Inject

@HiltViewModel
class InfoScreenViewModel @Inject constructor(
    observeWeightingsUseCase: ObserveWeightingsUseCase,
    observeSettingsUseCase: ObserveSettingsUseCase,
    private val calculateWeightingsChartUseCase: CalculateWeightingsChartUseCase,
) : ObservingViewModel<InfoScreenState, InfoObservingItem>(
    defaultState = InfoScreenState.Loading,
    flowProvider = FlowProvider(observeWeightingsUseCase, observeSettingsUseCase),
) {

    override suspend fun handleObserved(value: InfoObservingItem) {
        val settings = value.settings
        val weightings = value.weightings

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

    override fun onFailureObserved(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    private class FlowProvider(
        private val observeWeightingsUseCase: ObserveWeightingsUseCase,
        private val observeSettingsUseCase: ObserveSettingsUseCase,
    ) : ObservingFlowProvider<InfoObservingItem> {
        override fun provideObservingFlow(): Flow<InfoObservingItem> {
            return combine(
                observeWeightingsUseCase(),
                observeSettingsUseCase(),
                transform = { weightings, settings -> InfoObservingItem(weightings, settings) },
            )
        }
    }
}
