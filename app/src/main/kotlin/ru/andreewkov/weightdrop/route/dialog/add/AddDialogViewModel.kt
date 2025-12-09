package ru.andreewkov.weightdrop.route.dialog.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.ObserveWeightingsUseCase
import ru.andreewkov.weightdrop.domain.weighting.UpdateWeightingUseCase
import ru.andreewkov.weightdrop.route.base.ObservingFlowProvider
import ru.andreewkov.weightdrop.route.base.ObservingViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddDialogViewModel @Inject constructor(
    private val observeWeightingsUseCase: ObserveWeightingsUseCase,
    private val updateWeightingUseCase: UpdateWeightingUseCase,
) : ObservingViewModel<AddDialogState, List<Weighting>>(
        defaultState = AddDialogState.createDefault(),
        flowProvider = FlowProvider(observeWeightingsUseCase),
    ) {

    private val _datePickerRequestState = MutableStateFlow(AddDialogDatePickerRequestState(show = false))
    val datePickerRequestState get() = _datePickerRequestState.asStateFlow()

    private var weightings: List<Weighting> = emptyList()

    override suspend fun handleObserved(value: List<Weighting>) {
        weightings = value
        updateWeight()
    }

    override fun onFailureObserved(throwable: Throwable) {
        // TODO
    }

    private fun updateWeight(date: LocalDate = screenState.value.date) {
        viewModelScope.launch {
            val weighting = weightings.findLast { weighting ->
                weighting.date.isBefore(date) || weighting.date == date
            } ?: weightings.find { weighting ->
                weighting.date.isAfter(date)
            }
            weighting?.let {
                updateState {
                    copy(weight = weighting.value)
                }
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

    private class FlowProvider(
        private val observeWeightingsUseCase: ObserveWeightingsUseCase,
    ) : ObservingFlowProvider<List<Weighting>> {
        override fun provideObservingFlow(): Flow<List<Weighting>> {
            return observeWeightingsUseCase()
        }
    }
}
