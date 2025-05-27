package ru.andreewkov.weightdrop.ui.screen.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.GetWeightingUseCase
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getWeightingUseCase: GetWeightingUseCase,
) : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    fun initData() {
        getWeightingUseCase()
            .onSuccess { flow ->
                viewModelScope.launch {
                    flow.collect { weightings ->
                        handleWeightings(weightings)
                    }
                }
            }
            .onFailure {
                handleError()
            }
    }

    private fun handleWeightings(weightings: List<Weighting>) {
        val target = 90f
        _screenState.value = if (weightings.isEmpty()) {
            ScreenState.Empty
        } else {
            ScreenState.Chart(
                weightChart = weightChartCalculator.calculateWeightChart(target, weightings)
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