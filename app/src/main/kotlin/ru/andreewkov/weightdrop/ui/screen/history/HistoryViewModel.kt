package ru.andreewkov.weightdrop.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.DeleteWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingsUseCase
import ru.andreewkov.weightdrop.util.StateHiddenFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWeightingsUseCase: GetWeightingsUseCase,
    private val deleteWeightingUseCase: DeleteWeightingUseCase,
) : ViewModel() {

    val screenState = StateHiddenFlow<ScreenState>(ScreenState.Loading)

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

    fun onWeightingClick(value: Float, date: LocalDate) {
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
        getWeightingsUseCase()
            .onSuccess {
                viewModelScope.launch {
                    it.collect {
                        handleHistory(it)
                    }
                }
            }
            .onFailure {
                // TODO
            }
    }

    private fun handleHistory(weightings: List<Weighting>) {
        screenState.update(ScreenState.History(weightings))
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data class History(val weightings: List<Weighting>) : ScreenState()
    }
}
