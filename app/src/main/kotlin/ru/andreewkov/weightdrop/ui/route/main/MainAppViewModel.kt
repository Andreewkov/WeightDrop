package ru.andreewkov.weightdrop.ui.route.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.andreewkov.weightdrop.ui.route.AppAction
import ru.andreewkov.weightdrop.ui.route.AppActionHandler
import ru.andreewkov.weightdrop.ui.route.Route
import ru.andreewkov.weightdrop.util.ShareHiddenFlow
import ru.andreewkov.weightdrop.util.SignalFlow
import ru.andreewkov.weightdrop.util.StateHiddenFlow
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor() : ViewModel(), AppActionHandler {

    val navigationRoute = ShareHiddenFlow<Route>()
    val navigationOnBack = SignalFlow()

    val currentToolbarTitleRes = StateHiddenFlow(getStartBarItem().titleRes)
    val selectedNavigationScreenId = StateHiddenFlow(getStartBarItem().id)

    fun getStartBarItem(): Route.BarScreen = Route.startScreen

    override fun handleAction(action: AppAction) {
        when (action) {
            is AppAction.NavigateToRoute -> navigateTo(action.route)
            is AppAction.NavigateOnBack -> navigateOnBack()
        }
    }

    private fun navigateTo(route: Route) {
        navigationRoute.update(route)
        updateSelectedNavigationScreenId(route)
    }

    fun onCurrentRouteChanged(id: String?) {
        Route.findScreenOrNull(id)?.let { screen ->
            currentToolbarTitleRes.update(screen.titleRes)
            updateSelectedNavigationScreenId(screen)
        }
    }

    private fun navigateOnBack() {
        navigationOnBack.signal()
    }

    private fun updateSelectedNavigationScreenId(route: Route) {
        if (route is Route.BarScreen) {
            selectedNavigationScreenId.update(route.id)
        }
    }
}
