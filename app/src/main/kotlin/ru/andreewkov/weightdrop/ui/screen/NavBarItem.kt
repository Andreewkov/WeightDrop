package ru.andreewkov.weightdrop.ui.screen

sealed class NavBarItem {

    data object Main : NavBarItem()

    data object History : NavBarItem()
}