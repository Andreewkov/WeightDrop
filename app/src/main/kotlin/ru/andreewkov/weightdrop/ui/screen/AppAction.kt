package ru.andreewkov.weightdrop.ui.screen

sealed class AppAction {

    data class NavigationCLick(val screen: Screen) : AppAction()

    data object OnClickAdd : AppAction()

    data object OnDismissAdd : AppAction()

    data object OnValueAdded : AppAction()
}