package ru.andreewkov.weightdrop.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.UpdateWeightingUseCase
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val updateWeightingUseCase: UpdateWeightingUseCase,
) : ViewModel() {

    private val _showDateDialog = MutableStateFlow(false)
    val showDateDialog get() = _showDateDialog

    private val _screenState = MutableStateFlow(createDefaultScreenState())
    val screenState: StateFlow<ScreenState> get() = _screenState.asStateFlow()

    fun onDatePickerDialogRequest() {
        _showDateDialog.tryEmit(true)
    }

    fun onDatePickerDialogDismissRequest() {
        _showDateDialog.tryEmit(false)
    }

    fun onDatePickerDialogConfirm(date: LocalDate) {
        _showDateDialog.tryEmit(false)
        _screenState.tryEmit(_screenState.value.copy(date = date))
    }

    fun onWeightChanged(weight: Float) {
        _screenState.tryEmit(_screenState.value.copy(weight = weight))
    }

    fun onWeightAddClick() {
        viewModelScope.launch(Dispatchers.IO) {
            updateWeightingUseCase(
                Weighting(
                    value = _screenState.value.weight,
                    date = _screenState.value.date,
                ),
            )
        }
    }

    private fun createDefaultScreenState(): ScreenState {
        return ScreenState(
            date = LocalDate.now(),
            weight = 98.7f,
        )
    }

    data class ScreenState(
        val date: LocalDate,
        val weight: Float,
    )
}
