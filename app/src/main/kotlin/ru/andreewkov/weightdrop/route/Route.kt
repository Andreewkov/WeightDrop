package ru.andreewkov.weightdrop.route

import ru.andreewkov.weightdrop.R
import java.time.LocalDate

interface RouteParams

interface RouteResult

sealed class Route(
    open val id: String,
    open val navigationRoute: NavigationRoute,
) {

    open val params: RouteParams? = null

    open fun tryUpdateParams(result: RouteResult?) = Unit

    sealed class Screen(
        override val id: String,
        override val navigationRoute: NavigationRoute,
        open val titleRes: Int,
    ) : Route(id, navigationRoute)

    sealed class BarScreen(
        override val id: String,
        override val navigationRoute: NavigationRoute,
        override val titleRes: Int,
        val iconRes: Int,
    ) : Screen(id, navigationRoute, titleRes)

    sealed class Dialog(
        override val id: String,
        override val navigationRoute: NavigationRoute,
    ) : Route(id, navigationRoute)

    data object InfoScreen : BarScreen(
        id = "info",
        navigationRoute = NavigationRoute.Info,
        titleRes = R.string.screen_info,
        iconRes = R.drawable.ic_chart,
    )

    data object HistoryScreen : BarScreen(
        id = "history",
        navigationRoute = NavigationRoute.History,
        titleRes = R.string.screen_history,
        iconRes = R.drawable.ic_history,
    )

    data object SettingsScreen : BarScreen(
        id = "settings",
        navigationRoute = NavigationRoute.Settings,
        titleRes = R.string.screen_settings,
        iconRes = R.drawable.ic_settings,
    )

    data object AddDialog : Dialog(
        id = "add_dialog",
        navigationRoute = NavigationRoute.AddDialog,
    ) {

        data class Params(val date: LocalDate) : RouteParams

        override var params = Params(LocalDate.now())

        override fun tryUpdateParams(result: RouteResult?) {
            when (result) {
                is DateDialog.Result -> params = Params(result.date)
            }
        }
    }

    data object DateDialog : Dialog(
        id = "date_dialog",
        navigationRoute = NavigationRoute.DateDialog,
    ) {

        data class Params(val date: LocalDate) : RouteParams

        data class Result(val date: LocalDate) : RouteResult

        override var params = Params(LocalDate.now())
    }

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
