package ru.andreewkov.weightdrop.route.screen.info

import ru.andreewkov.weightdrop.WeightChart
import ru.andreewkov.weightdrop.route.base.BaseScreenState

sealed class InfoScreenState : BaseScreenState {

    data object Loading : InfoScreenState()

    data object Empty : InfoScreenState()

    data class Chart(val weightChart: WeightChart) : InfoScreenState()
}
