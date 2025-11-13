package ru.andreewkov.weightdrop.route.dialog.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.UpdateWeightingUseCase
import ru.andreewkov.weightdrop.route.base.ScreenStateViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddDialogViewModel @Inject constructor(
    private val getWeightingUseCase: GetWeightingUseCase,
    private val updateWeightingUseCase: UpdateWeightingUseCase,
) : ScreenStateViewModel<AddDialogState>(
    defaultState = AddDialogState.createDefault(),
) {

    private val _datePickerRequestState = MutableStateFlow(AddDialogDatePickerRequestState(show = false))
    val datePickerRequestState get() = _datePickerRequestState.asStateFlow()

    private fun updateWeight(date: LocalDate) {
        viewModelScope.launch {
            val weighting = getWeightingUseCase(date).getOrElse {
                // updateState { copy(weight = 0f) }
                return@launch
            } // TODO
            updateState {
                copy(
                    date = weighting.date,
                    weight = weighting.value,
                )
            }
        }
    }

    fun onDateChanged(date: LocalDate) {
        updateState { copy(date = date) }
        updateWeight(date)
    }

    fun onDateClick(date: LocalDate) {
        _datePickerRequestState.value = AddDialogDatePickerRequestState(
            show = true,
            date = date,
        )
    }

    fun onDatePickerDismissRequest() {
        _datePickerRequestState.value = AddDialogDatePickerRequestState(show = false)
    }

    fun onWeightChanged(weight: Float) {
        updateState { copy(weight = weight) }
    }

    fun onWeightAddClick() {
        val state = screenState.value
        viewModelScope.launch(Dispatchers.IO) {
            updateWeightingUseCase(
                Weighting(
                    value = state.weight,
                    date = state.date,
                ),
            )
        }
    }

    fun dispose() {
        resetState()
        _datePickerRequestState.value = AddDialogDatePickerRequestState(show = false)
    }
}
