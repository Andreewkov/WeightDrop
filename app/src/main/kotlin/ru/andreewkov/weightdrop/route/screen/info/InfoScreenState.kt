package ru.andreewkov.weightdrop.route.screen.info

import ru.andreewkov.weightdrop.domain.model.Settings
import ru.andreewkov.weightdrop.domain.model.WeightingsChart
import ru.andreewkov.weightdrop.route.base.ScreenState

sealed class InfoScreenState : ScreenState {

    data object Loading : InfoScreenState()

    data object Failure : InfoScreenState()

    data object SuccessEmpty : InfoScreenState()

    data class SuccessChart(val chart: WeightingsChart, val settings: Settings) : InfoScreenState()
}
