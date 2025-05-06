package ru.andreewkov.weightdrop.ui.screen.info

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.andreewkov.weightdrop.ui.WeightChart
import ru.andreewkov.weightdrop.ui.WeightChartCalculator
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumFirst
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumFourth
import ru.andreewkov.weightdrop.ui.util.stubWeightingsMediumSecond
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor() : ViewModel() {

    private val weightChartCalculator = WeightChartCalculator()
    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    init {
        initData()
    }

    fun initData() {
        val target = 90f
        _screenState.value = ScreenState.Chart(
            weightChart = weightChartCalculator.calculateWeightChart(target, stubWeightingsMediumFirst)
        )
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data class Chart(val weightChart: WeightChart) : ScreenState()
    }
}