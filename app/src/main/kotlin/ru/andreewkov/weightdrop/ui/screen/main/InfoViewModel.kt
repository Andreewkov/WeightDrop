package ru.andreewkov.weightdrop.ui.screen.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumFourth

class InfoViewModel : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    fun initData() {
        val target = 80f
        _screenState.value = ScreenState.Chart(
            weightChart = weightChartCalculator.calculateWeightChart(target, stubWeightingsMediumFourth)
        )
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data class Chart(val weightChart: WeightChart) : ScreenState()
    }
}