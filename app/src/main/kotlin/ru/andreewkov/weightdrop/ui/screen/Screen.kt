package ru.andreewkov.weightdrop.ui.screen

import ru.andreewkov.weightdrop.R

sealed class Screen(
    open val id: String,
    open val titleRes: Int,
) {

    sealed class NavigationBarItem(
        override val id: String,
        override val titleRes: Int,
        val iconRes: Int,
    ) : Screen(id, titleRes)

    data object Info : NavigationBarItem(
        id = "info",
        titleRes = R.string.screen_info,
        iconRes = R.drawable.ic_chart,
    )

    data object History : NavigationBarItem(
        id = "history",
        titleRes = R.string.screen_history,
        iconRes = R.drawable.ic_history,
    )

    data object Settings : NavigationBarItem(
        id = "Settings",
        titleRes = R.string.screen_settings,
        iconRes = R.drawable.ic_settings,
    )

    data object Add : Screen(
        id = "Add",
        titleRes = R.string.screen_add,
    )

    companion object {

        fun getStartScreen() = Info

        fun findScreen(id: String?): Screen {
            return getAll().find { it.id == id } ?: getStartScreen()
        }

        fun getNavigationBarItems() = listOf(
            Info,
            History,
            Settings,
        )

        fun getAll() = listOf(
            Info,
            History,
            Settings,
        )
    }
}
