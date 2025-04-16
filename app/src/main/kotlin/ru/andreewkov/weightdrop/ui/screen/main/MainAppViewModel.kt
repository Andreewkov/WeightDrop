package ru.andreewkov.weightdrop.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Screen
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor() : ViewModel(), AppActionHandler {

    private val _navigationScreen = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val navigationScreen get() = _navigationScreen.asSharedFlow()

    fun getStartNavigationScreen() = Screen.Info

    override fun handleAction(action: AppAction) {
        when (action) {
            AppAction.ClickAdd -> onClickAdd()
        }
    }

    private fun onClickAdd() {
        _navigationScreen.tryEmit(Screen.Add)
    }
}
