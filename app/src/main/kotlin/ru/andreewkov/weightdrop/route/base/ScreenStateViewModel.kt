package ru.andreewkov.weightdrop.route.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ScreenState

abstract class ScreenStateViewModel<State : ScreenState>(
    private val defaultState: State,
) : ViewModel() {

    private val _screenState = MutableStateFlow(defaultState)
    val screenState get() = _screenState.asStateFlow()

    protected fun updateState(action: State.() -> State) {
        _screenState.update { state ->
            action(state)
        }
    }

    protected fun resetState() {
        _screenState.value = defaultState
    }
}
