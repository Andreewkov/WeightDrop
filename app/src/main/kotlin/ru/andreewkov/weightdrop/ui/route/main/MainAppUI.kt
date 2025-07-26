package ru.andreewkov.weightdrop.ui.route.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.andreewkov.weightdrop.ui.route.NavigationRoute
import ru.andreewkov.weightdrop.ui.route.Route
import ru.andreewkov.weightdrop.ui.route.RouteParams
import ru.andreewkov.weightdrop.ui.route.dialog.add.AddDialogUI
import ru.andreewkov.weightdrop.ui.route.dialog.date.DatePickerDialogUI
import ru.andreewkov.weightdrop.ui.route.screen.history.HistoryScreenUI
import ru.andreewkov.weightdrop.ui.route.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.ui.route.screen.settings.SettingsScreenUI
import ru.andreewkov.weightdrop.ui.widget.NavigationBarColors
import ru.andreewkov.weightdrop.ui.widget.NavigationBarWidget
import ru.andreewkov.weightdrop.ui.widget.ToolbarWidget
import ru.andreewkov.weightdrop.ui.widget.ToolbarWidgetColors

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: MainAppViewModel = hiltViewModel()
    val toolbarTitleRes by viewModel.currentToolbarTitleRes.get().collectAsState()
    val selectedNavigationScreenId by viewModel.selectedNavigationScreenId.get().collectAsState()
    var currentRouteParams: RouteParams? = remember { null }

    Init(
        viewModel = viewModel,
        navController = navController,
        onRouteParamsChanged = { parans ->
            currentRouteParams = parans
        },
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarWidget(
                titleRes = toolbarTitleRes,
                actionHandler = viewModel,
                colors = ToolbarWidgetColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        bottomBar = {
            NavigationBarWidget(
                actionHandler = viewModel,
                items = Route.getBarItems(),
                colors = NavigationBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    activeItemColor = MaterialTheme.colorScheme.secondary,
                    inactiveItemColor = MaterialTheme.colorScheme.primary,
                ),
                isNavigationBarItemSelected = { item -> selectedNavigationScreenId == item.id },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = viewModel.getStartBarItem().id,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = NavigationRoute.Info.destination) {
                InfoScreenUI()
            }
            composable(route = NavigationRoute.History.destination) {
                HistoryScreenUI(actionHandler = viewModel)
            }
            composable(route = NavigationRoute.Settings.destination) {
                SettingsScreenUI()
            }
            dialog(route = NavigationRoute.AddDialog.destination) { backStackEntry ->
                AddDialogUI(
                    paramsProvider = { currentRouteParams as Route.AddDialog.Params },
                    actionHandler = viewModel,
                )
            }
            dialog(route = NavigationRoute.DateDialog.destination) { backStackEntry ->
                DatePickerDialogUI(
                    paramsProvider = { currentRouteParams as Route.DateDialog.Params },
                    actionHandler = viewModel,
                )
            }
        }
    }
}

@Composable
private fun Init(
    viewModel: MainAppViewModel,
    navController: NavHostController,
    onRouteParamsChanged: (RouteParams?) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(backStackEntry) {
        viewModel.onCurrentRouteChanged(backStackEntry?.destination?.route)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationRoute.get().onEach { route ->
            if (route.id == backStackEntry?.destination?.route) return@onEach

            onRouteParamsChanged(route.params)
            navController.navigate(route = route.id) {
                launchSingleTop = true
                restoreState = true
            }
        }.launchIn(coroutineScope)

        viewModel.navigationOnBack.get().onEach { route ->
            navController.popBackStack()
        }.launchIn(coroutineScope)
    }
}
