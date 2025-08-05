package ru.andreewkov.weightdrop.navigation

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import ru.andreewkov.weightdrop.route.NavigationRoute
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.route.RouteParams
import ru.andreewkov.weightdrop.route.RouteResult
import ru.andreewkov.weightdrop.util.SignalFlow
import java.time.LocalDate

interface Navigation {

    val navigateToRoute: SharedFlow<NavigationRoute>
    val navigateOnBack: SignalFlow
    val toolbarTitleRes: StateFlow<Int>
    val selectedNavigationRoute: StateFlow<NavigationRoute>

    val currentRouteParams: RouteParams?

    fun getStartBarScreen(): Route.BarScreen

    fun back(result: RouteResult? = null): Boolean

    fun openInfoScreen()

    fun openHistoryScreen()

    fun openSettingsScreen()

    fun openAddDialog(date: LocalDate = LocalDate.now())

    fun openDatePickerDialog(date: LocalDate = LocalDate.now())
}
