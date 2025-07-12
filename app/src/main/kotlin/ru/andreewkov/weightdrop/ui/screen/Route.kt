package ru.andreewkov.weightdrop.ui.screen

import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.Route.AddDialog.Params
import java.time.LocalDate

interface RouteParams

sealed class Route(
    open val id: String,
    open val navigationRoute: NavigationRoute,
    open val params: RouteParams? = null,
) {

    sealed class Screen(
        override val id: String,
        override val navigationRoute: NavigationRoute,
        open val titleRes: Int,
        override val params: RouteParams? = null,
    ): Route(id, navigationRoute, params)

    sealed class BarScreen(
        override val id: String,
        override val navigationRoute: NavigationRoute,
        override val titleRes: Int,
        val iconRes: Int,
        override val params: RouteParams? = null,
    ) : Screen(id, navigationRoute, titleRes, params)

    sealed class Dialog(
        override val id: String,
        override val navigationRoute: NavigationRoute,
        override val params: RouteParams? = null,
    ): Route(id, navigationRoute, params)

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

    data class AddDialog(
        override val params: Params,
    ) : Dialog(
        id = "add_dialog",
        navigationRoute = NavigationRoute.AddDialog,
        params = params,
    ) {
        data class Params(val date: LocalDate) : RouteParams
    }

    data class DateDialog(
        override val params: Params,
    ) : Dialog(
        id = "date_dialog",
        navigationRoute = NavigationRoute.DateDialog,
        params = params,
    ) {
        data class Params(
            val date: LocalDate,
            val resultHandler: ResultHandler,
        ) : RouteParams

        interface ResultHandler {
            fun onDateDialogResult(date: LocalDate)
        }
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
