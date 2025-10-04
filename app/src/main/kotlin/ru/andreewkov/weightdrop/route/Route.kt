package ru.andreewkov.weightdrop.route

import ru.andreewkov.weightdrop.R

interface RouteParams

interface RouteResult

sealed class Route(
    open val id: String,
) {

    open val params: RouteParams? = null

    open fun tryUpdateParams(result: RouteResult?) = Unit

    sealed class Screen(
        override val id: String,
        open val titleRes: Int,
    ) : Route(id)

    sealed class BarScreen(
        override val id: String,
        override val titleRes: Int,
        val iconRes: Int,
    ) : Screen(id, titleRes)

    data object InfoScreen : BarScreen(
        id = "info",
        titleRes = R.string.screen_info,
        iconRes = R.drawable.ic_chart,
    )

    data object HistoryScreen : BarScreen(
        id = "history",
        titleRes = R.string.screen_history,
        iconRes = R.drawable.ic_history,
    )

    data object SettingsScreen : BarScreen(
        id = "settings",
        titleRes = R.string.screen_settings,
        iconRes = R.drawable.ic_settings,
    )

    companion object {

        val startScreen = InfoScreen

        private val allScreens = listOf(
            InfoScreen,
            HistoryScreen,
            SettingsScreen,
        )

        fun findScreenOrNull(id: String?): Screen? {
            return allScreens.find { it.id == id }
        }

        fun getBarItems() = listOf(
            InfoScreen,
            HistoryScreen,
            SettingsScreen,
        )
    }
}
