package ru.andreewkov.weightdrop.route.screen.history

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.CalculateHistoryBlocksUseCase
import ru.andreewkov.weightdrop.domain.weighting.DeleteWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.ObserveWeightingsUseCase
import ru.andreewkov.weightdrop.route.base.ObservingFlowProvider
import ru.andreewkov.weightdrop.route.base.ObservingViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    observeWeightingsUseCase: ObserveWeightingsUseCase,
    private val calculateHistoryBlocksUseCase: CalculateHistoryBlocksUseCase,
    private val deleteWeightingUseCase: DeleteWeightingUseCase,
) : ObservingViewModel<HistoryScreenState, List<Weighting>>(
    defaultState = HistoryScreenState.Loading,
    flowProvider = FlowProvider(observeWeightingsUseCase),
) {

    fun onWeightingSwiped(value: Float, date: LocalDate) {
        viewModelScope.launch {
            deleteWeightingUseCase(
                Weighting(
                    value = value,
                    date = date,
                ),
            )
        }
    }

    override suspend fun handleObserved(value: List<Weighting>) {
        if (value.isEmpty()) {
            updateState { HistoryScreenState.Empty }
            return
        }

        viewModelScope.launch {
            val blocks = calculateHistoryBlocksUseCase(value).getOrElse {
                updateState { HistoryScreenState.Failure }
                return@launch
            }

            updateState { HistoryScreenState.Success(blocks) }
        }
    }

    override fun onFailureObserved(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    private class FlowProvider(
        private val observeWeightingsUseCase: ObserveWeightingsUseCase,
    ) : ObservingFlowProvider<List<Weighting>> {
        override fun provideObservingFlow(): Flow<List<Weighting>> {
            return observeWeightingsUseCase()
        }
    }
}
