package ru.andreewkov.weightdrop.route.screen.info

import ru.andreewkov.weightdrop.WeightChart

sealed class InfoScreenState {

    data object Loading : InfoScreenState()

    data object Empty : InfoScreenState()

    data class Chart(val weightChart: WeightChart) : InfoScreenState()
}
