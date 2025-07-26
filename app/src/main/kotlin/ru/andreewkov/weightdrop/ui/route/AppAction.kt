package ru.andreewkov.weightdrop.ui.route

sealed class AppAction {

    data class NavigateToRoute(val route: Route) : AppAction()

    data object NavigateOnBack : AppAction()
}
