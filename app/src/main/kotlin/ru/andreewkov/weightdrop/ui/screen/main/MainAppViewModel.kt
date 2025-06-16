package ru.andreewkov.weightdrop.ui.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.AppActionHandler
import ru.andreewkov.weightdrop.ui.screen.Screen
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor() : ViewModel(), AppActionHandler {

    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog get() = _showAddDialog.asStateFlow()
    private val _navigationScreen = MutableSharedFlow<Screen>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val navigationScreen get() = _navigationScreen.asSharedFlow()

    fun getStartNavigationScreen(): Screen = Screen.getStartScreen()

    override fun handleAction(action: AppAction) {
        when (action) {
            is AppAction.NavigationCLick -> when (action.screen) {
                Screen.Info -> onClickInfo()
                Screen.History -> onClickHistory()
                Screen.Settings -> onClickSettings()
            }
            AppAction.OnClickAdd -> onClickAdd()
            AppAction.OnDismissRequestAddDialog -> onDismissRequestAddDialog()
            AppAction.OnValueAddFromDialog -> onValueAddFromDialog()
        }
    }

    private fun onClickInfo() {
        _navigationScreen.tryEmit(Screen.Info)
    }

    private fun onClickHistory() {
        _navigationScreen.tryEmit(Screen.History)
    }

    private fun onClickSettings() {
        // _navigationScreen.tryEmit(Screen.History)
    }

    private fun onClickAdd() {
        _showAddDialog.tryEmit(true)
    }

    private fun onDismissRequestAddDialog() {
        _showAddDialog.tryEmit(false)
    }

    private fun onValueAddFromDialog() {
        _showAddDialog.tryEmit(false)
    }
}
