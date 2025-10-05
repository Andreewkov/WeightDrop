package ru.andreewkov.weightdrop.route.main

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.route.base.BaseViewModel
import ru.andreewkov.weightdrop.utils.MutableSignalFlow
import ru.andreewkov.weightdrop.utils.asSignalFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor() : BaseViewModel<MainAppState>(
    defaultState = MainAppState.createDefault(),
) {

    private val _navigateToRouteId = MutableSharedFlow<String>()
    val navigateToRouteId get() = _navigateToRouteId.asSharedFlow()

    private val _navigateOnBack = ru.andreewkov.weightdrop.utils.MutableSignalFlow()
    val navigateOnBack get() = _navigateOnBack.asSignalFlow()

    private val _addRequestState = MutableStateFlow(MainAppAddRequestState(show = false))
    val addRequestState get() = _addRequestState.asStateFlow()

    fun onBarScreenClick(screen: Route.BarScreen) {
        viewModelScope.launch {
            _navigateToRouteId.emit(screen.id)
        }
    }

    fun onAddClick() {
        _addRequestState.value = MainAppAddRequestState(show = true)
    }

    fun onHistoryCardClick(date: LocalDate) {
        _addRequestState.value = MainAppAddRequestState(
            show = true,
            date = date,
        )
    }

    fun onAddDismissRequest() {
        _addRequestState.value = MainAppAddRequestState(show = false)
    }

    fun onRouteIdUpdated(id: String) {
        val route = Route.findScreenOrNull(id) ?: Route.startScreen
        updateState {
            copy(
                toolbarTitleRes = route.titleRes,
                selectedBarScreen = route as? Route.BarScreen ?: selectedBarScreen,
            )
        }
    }
}
