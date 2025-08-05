package ru.andreewkov.weightdrop.route.screen.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow<SettingsScreenState>(SettingsScreenState.Loading)
    val screenState get() = _screenState.asStateFlow()
}
