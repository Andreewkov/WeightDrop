package ru.andreewkov.weightdrop.route.screen.history

import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.route.base.BaseScreenState

sealed class HistoryScreenState : BaseScreenState {

    data object Loading : HistoryScreenState()

    data object Empty : HistoryScreenState()

    data class History(val weightings: List<Weighting>) : HistoryScreenState()
}
