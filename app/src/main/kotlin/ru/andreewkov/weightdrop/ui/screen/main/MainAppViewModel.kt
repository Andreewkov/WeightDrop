package ru.andreewkov.weightdrop.ui.screen.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.andreewkov.weightdrop.ui.screen.Screen

class MainAppViewModel : ViewModel() {

    private val _navigationScreen = MutableSharedFlow<Screen>()
    val navigationScreen get() = _navigationScreen.asSharedFlow()

    fun getStartNavigationScreen() = Screen.Info
}