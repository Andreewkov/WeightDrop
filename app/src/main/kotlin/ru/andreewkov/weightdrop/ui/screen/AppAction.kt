package ru.andreewkov.weightdrop.ui.screen

sealed class AppAction {

    data class NavigateToRoute(val route: Route) : AppAction()

    data object NavigateOnBack : AppAction()
}
