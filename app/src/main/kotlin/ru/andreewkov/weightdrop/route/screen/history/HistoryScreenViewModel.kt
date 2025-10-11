package ru.andreewkov.weightdrop.route.screen.history

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.CalculateHistoryBlocksUseCase
import ru.andreewkov.weightdrop.domain.weighting.DeleteWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.ObserveWeightingsUseCase
import ru.andreewkov.weightdrop.route.base.ScreenStateViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val observeWeightingsUseCase: ObserveWeightingsUseCase,
    private val calculateHistoryBlocksUseCase: CalculateHistoryBlocksUseCase,
    private val deleteWeightingUseCase: DeleteWeightingUseCase,
) : ScreenStateViewModel<HistoryScreenState>(
        defaultState = HistoryScreenState.Loading,
    ) {

    init {
        loadHistory()
    }

    fun onWeightingDeleted(value: Float, date: LocalDate) {
        viewModelScope.launch {
            deleteWeightingUseCase(
                Weighting(
                    value = value,
                    date = date,
                ),
            )
        }
    }

    private fun loadHistory() {
        observeWeightingsUseCase()
            .onSuccess {
                viewModelScope.launch {
                    it.collect {
                        handleHistory(it) // TODO
                    }
                }
            }
            .onFailure {
                // TODO
            }
    }

    private fun handleHistory(weightings: List<Weighting>) {
        if (weightings.isEmpty()) {
            updateState { HistoryScreenState.Empty }
            return
        }

        viewModelScope.launch {
            val blocks = calculateHistoryBlocksUseCase(weightings).getOrElse {
                updateState { HistoryScreenState.Failure }
                return@launch
            }

            updateState { HistoryScreenState.Success(blocks) }
        }
    }
}
