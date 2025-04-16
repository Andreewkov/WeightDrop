package ru.andreewkov.weightdrop.ui.screen.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow(createDefaultScreenState())
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    private fun createDefaultScreenState(): ScreenState {
        return ScreenState(
            date = LocalDate.now(),
            weight = null,
        )
    }

    data class ScreenState(
        val date: LocalDate,
        val weight: Float?,
    )
}