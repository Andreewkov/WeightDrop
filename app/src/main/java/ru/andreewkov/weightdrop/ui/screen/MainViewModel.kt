package ru.andreewkov.weightdrop.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.andreewkov.weightdrop.model.Weighting
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import java.time.LocalDate

class MainViewModel : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    fun initData() {
        val target = 70f
        val weights = listOf(
            Weighting(73.7f, LocalDate.parse("2024-06-18")),
            Weighting(83.1f, LocalDate.parse("2024-07-01")),
            Weighting(72.0f, LocalDate.parse("2024-07-03")),
            Weighting(72.7f, LocalDate.parse("2025-01-20")),
            Weighting(70.1f, LocalDate.parse("2025-01-27")),
        )
        _screenState.value = ScreenState.Chart(
            weightChart = weightChartCalculator.calculateWeightChart(target, weights)
        )
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data class Chart(val weightChart: WeightChart) : ScreenState()
    }

}