package ru.andreewkov.weightdrop.ui.screen.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.andreewkov.weightdrop.model.Weighting
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import java.time.LocalDate

class InfoViewModel : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    fun initData() {
        val target = 80f
        val weights = listOf(
            Weighting(93.7f, LocalDate.parse("2024-10-18")),
            Weighting(93.9f, LocalDate.parse("2024-10-19")),
            Weighting(93.7f, LocalDate.parse("2024-10-20")),
            Weighting(92.3f, LocalDate.parse("2024-10-21")),
            Weighting(92.0f, LocalDate.parse("2024-10-24")),
            Weighting(92.1f, LocalDate.parse("2024-10-25")),
            Weighting(92.7f, LocalDate.parse("2024-10-26")),
            Weighting(92.8f, LocalDate.parse("2024-10-27")),
            Weighting(91.2f, LocalDate.parse("2024-10-28")),
            Weighting(90.7f, LocalDate.parse("2024-10-30")),
            Weighting(88.1f, LocalDate.parse("2024-11-01")),
            Weighting(88.9f, LocalDate.parse("2024-11-02")),
            Weighting(87.7f, LocalDate.parse("2024-11-03")),
            Weighting(87.1f, LocalDate.parse("2024-11-05")),
            Weighting(86.2f, LocalDate.parse("2024-11-06")),
            Weighting(85.1f, LocalDate.parse("2024-11-09")),
            Weighting(85.4f, LocalDate.parse("2024-11-14")),
            Weighting(83.9f, LocalDate.parse("2024-11-16")),
            Weighting(83.8f, LocalDate.parse("2024-11-19")),
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