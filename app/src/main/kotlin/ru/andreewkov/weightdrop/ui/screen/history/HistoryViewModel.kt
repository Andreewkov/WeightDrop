package ru.andreewkov.weightdrop.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.domain.GetWeightingUseCase
import ru.andreewkov.weightdrop.domain.model.Weighting
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWeightingUseCase: GetWeightingUseCase,
) : ViewModel() {

    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    val screenState get() = _screenState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        getWeightingUseCase()
            .onSuccess {
                viewModelScope.launch {
                    it.collect {
                        handleHistory(it)
                    }
                }
            }
    }

    private fun handleHistory(weightings: List<Weighting>) {
        _screenState.value = ScreenState.History(weightings)
    }

    sealed class ScreenState {

        data object Loading : ScreenState()

        data class History(val weightings: List<Weighting>) : ScreenState()
    }
}