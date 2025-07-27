package ru.andreewkov.weightdrop.route.dialog.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.model.Weighting
import ru.andreewkov.weightdrop.domain.weighting.GetWeightingUseCase
import ru.andreewkov.weightdrop.domain.weighting.UpdateWeightingUseCase
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.util.StateHiddenFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val getWeightingUseCase: GetWeightingUseCase,
    private val updateWeightingUseCase: UpdateWeightingUseCase,
) : ViewModel(), Route.DateDialog.ResultHandler {

    val screenState = StateHiddenFlow(createDefaultScreenState())

    fun setInitialDate(date: LocalDate) {
        updateScreenState(date = date)
        updateWeight(date)
    }

    override fun onDateDialogResult(date: LocalDate) {
        updateScreenState(date = date)
        updateWeight(date)
    }

    private fun updateWeight(date: LocalDate) {
        viewModelScope.launch {
            val weighting = getWeightingUseCase(date).getOrElse { return@launch } // TODO
            updateScreenState(date = weighting.date, weight = weighting.value)
        }
    }

    fun onWeightChanged(weight: Float) {
        updateScreenState(weight = weight)
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

    private fun createDefaultScreenState(): ScreenState {
        return ScreenState(
            date = LocalDate.now(),
            weight = 0f,
        )
    }

    private fun updateScreenState(
        date: LocalDate = screenState.value.date,
        weight: Float = screenState.value.weight,
    ) {
        screenState.update(ScreenState(date, weight))
    }

    data class ScreenState(
        val date: LocalDate,
        val weight: Float,
    )
}
