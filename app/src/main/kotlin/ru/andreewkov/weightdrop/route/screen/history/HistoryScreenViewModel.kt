package ru.andreewkov.weightdrop.route.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.DeleteWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingsUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val getWeightingsUseCase: GetWeightingsUseCase,
    private val deleteWeightingUseCase: DeleteWeightingUseCase,
) : ViewModel() {

    private val _screenState = MutableStateFlow<HistoryScreenState>(HistoryScreenState.Loading)
    val screenState get() = _screenState.asStateFlow()

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
        _screenState.value = if (weightings.isNotEmpty()) {
            HistoryScreenState.History(weightings)
        } else {
            HistoryScreenState.Empty
        }
    }
}
