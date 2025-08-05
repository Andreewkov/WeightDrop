package ru.andreewkov.weightdrop.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.andreewkov.weightdrop.route.NavigationRoute
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.route.RouteParams
import ru.andreewkov.weightdrop.route.RouteResult
import ru.andreewkov.weightdrop.util.MutableSignalFlow
import ru.andreewkov.weightdrop.util.asSignalFlow
import ru.andreewkov.weightdrop.util.signal
import java.time.LocalDate
import javax.inject.Inject

class NavigationHolder @Inject constructor() : Navigation {

    private val _navigateToRoute = MutableSharedFlow<NavigationRoute>()
    override val navigateToRoute get() = _navigateToRoute.asSharedFlow()

    private val _navigateOnBack = MutableSignalFlow()
    override val navigateOnBack get() = _navigateOnBack.asSignalFlow()

    private val _toolbarTitleRes = MutableStateFlow(getStartBarScreen().titleRes)
    override val toolbarTitleRes get() = _toolbarTitleRes.asStateFlow()

    private val _selectedNavigationRoute = MutableStateFlow(getStartBarScreen().navigationRoute)
    override val selectedNavigationRoute get() = _selectedNavigationRoute.asStateFlow()

    private var _currentRouteParams: RouteParams? = null
    override val currentRouteParams get() = _currentRouteParams

    private val routeStack = ArrayDeque<Route>()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        getStartBarScreen().putToStack()
    }

    override fun getStartBarScreen() = Route.InfoScreen

    override fun back(result: RouteResult?): Boolean {
        popStack()
        if (isStackEmpty()) return false

        val currentRoute = getHeadFromStack()
        scope.launch {
            update(currentRoute, result)
            _navigateOnBack.signal()
        }

        return true
    }

    override fun openInfoScreen() {
        Route.InfoScreen.navigate()
    }

    override fun openHistoryScreen() {
        Route.HistoryScreen.navigate()
    }

    override fun openSettingsScreen() {
        Route.SettingsScreen.navigate()
    }

    override fun openAddDialog(date: LocalDate) {
        Route.AddDialog.apply {
            params = Route.AddDialog.Params(date)
            navigate()
        }
    }

    override fun openDatePickerDialog(date: LocalDate) {
        Route.DateDialog.apply {
            params = Route.DateDialog.Params(date)
            navigate()
        }
    }

    private fun Route.navigate() {
        putToStack()

        scope.launch {
            update(this@navigate)
            _navigateToRoute.emit(navigationRoute)
        }
    }

    private suspend fun update(route: Route, result: RouteResult? = null) {
        if (route is Route.Screen) {
            _toolbarTitleRes.emit(route.titleRes)
        }
        if (route is Route.BarScreen) {
            _selectedNavigationRoute.emit(route.navigationRoute)
        }
        route.tryUpdateParams(result)
        _currentRouteParams = route.params
    }

    private fun Route.putToStack() = routeStack.addLast(this)

    private fun popStack() = routeStack.removeLast()

    private fun getHeadFromStack() = routeStack.last()

    private fun isStackEmpty() = routeStack.isEmpty()
}
