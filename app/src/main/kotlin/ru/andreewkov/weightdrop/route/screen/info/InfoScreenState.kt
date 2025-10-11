package ru.andreewkov.weightdrop.route.screen.info

import ru.andreewkov.weightdrop.WeightChart
import ru.andreewkov.weightdrop.route.base.ScreenState

sealed class InfoScreenState : ScreenState {

    data object Loading : InfoScreenState()

    data object Failure : InfoScreenState()

    data object SuccessEmpty : InfoScreenState()

    data class SuccessChart(val weightChart: WeightChart) : InfoScreenState()
}
