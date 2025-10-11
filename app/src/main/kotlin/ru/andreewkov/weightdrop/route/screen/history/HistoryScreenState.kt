package ru.andreewkov.weightdrop.route.screen.history

import ru.andreewkov.weightdrop.domain.model.HistoryBlock
import ru.andreewkov.weightdrop.route.base.ScreenState

sealed class HistoryScreenState : ScreenState {

    data object Loading : HistoryScreenState()

    data object Failure : HistoryScreenState()

    data object Empty : HistoryScreenState()

    data class Success(val blocks: List<HistoryBlock>) : HistoryScreenState()
}
