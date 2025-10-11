package ru.andreewkov.weightdrop.route.screen.info

import ru.andreewkov.weightdrop.WeightChart
import ru.andreewkov.weightdrop.route.base.ScreenState

sealed class InfoScreenState : ScreenState {

    data object Loading : InfoScreenState()

    data object Empty : InfoScreenState()

    data class Chart(val weightChart: WeightChart) : InfoScreenState()
}
