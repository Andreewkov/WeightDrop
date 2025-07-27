package ru.andreewkov.weightdrop.route

sealed class AppAction {

    data class NavigateToRoute(val route: Route) : AppAction()

    data object NavigateOnBack : AppAction()
}
