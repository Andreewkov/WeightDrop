package ru.andreewkov.weightdrop.ui.screen

sealed class AppAction {

    data class NavigationCLick(val screen: Screen) : AppAction()

    data object OnClickAdd : AppAction()

    data object OnDismissRequestAddDialog : AppAction()

    data object OnValueAddFromDialog : AppAction()
}