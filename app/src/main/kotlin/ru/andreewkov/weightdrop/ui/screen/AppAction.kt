package ru.andreewkov.weightdrop.ui.screen

sealed class AppAction {

    data class NavigationCLick(val screen: Screen) : AppAction()
}
