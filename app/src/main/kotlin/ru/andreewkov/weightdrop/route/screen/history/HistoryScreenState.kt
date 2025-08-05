package ru.andreewkov.weightdrop.route.screen.history

import ru.andreewkov.weightdrop.domain.model.Weighting

sealed class HistoryScreenState {

    data object Loading : HistoryScreenState()

    data object Empty : HistoryScreenState()

    data class History(val weightings: List<Weighting>) : HistoryScreenState()
}
