package ru.andreewkov.weightdrop.route.main

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
import ru.andreewkov.weightdrop.navigation.Navigation
import ru.andreewkov.weightdrop.route.NavigationRoute
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.route.dialog.add.AddDialogUI
import ru.andreewkov.weightdrop.route.dialog.date.DatePickerDialogUI
import ru.andreewkov.weightdrop.route.screen.history.HistoryScreenUI
import ru.andreewkov.weightdrop.route.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.route.screen.settings.SettingsScreenUI
import ru.andreewkov.weightdrop.widget.NavigationBarColors
import ru.andreewkov.weightdrop.widget.NavigationBarWidget
import ru.andreewkov.weightdrop.widget.ToolbarWidget
import ru.andreewkov.weightdrop.widget.ToolbarWidgetColors

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: MainAppViewModel = hiltViewModel()
    val navigation = remember { viewModel.navigation }
    val toolbarTitleRes by navigation.toolbarTitleRes.collectAsState()
    val selectedNavigationRoute by navigation.selectedNavigationRoute.collectAsState()

    Init(
        navigation = navigation,
        navController = navController,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarWidget(
                titleRes = toolbarTitleRes,
                colors = ToolbarWidgetColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                onAddClick = {
                    navigation.openAddDialog()
                },
            )
        },
        bottomBar = {
            NavigationBarWidget(
                items = Route.getBarItems(),
                colors = NavigationBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    activeItemColor = MaterialTheme.colorScheme.secondary,
                    inactiveItemColor = MaterialTheme.colorScheme.primary,
                ),
                onBarItemClick = { barScreen ->
                    navigation.apply {
                        when (barScreen) {
                            Route.InfoScreen -> openInfoScreen()
                            Route.HistoryScreen -> openHistoryScreen()
                            Route.SettingsScreen -> openSettingsScreen()
                        }
                    }
                },
                isBarItemSelected = { item -> selectedNavigationRoute.destination == item.navigationRoute.destination },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = navigation.getStartBarScreen().id,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = NavigationRoute.Info.destination) {
                InfoScreenUI()
            }
            composable(route = NavigationRoute.History.destination) {
                HistoryScreenUI(
                    onCardClick = { date ->
                        navigation.openAddDialog(date)
                    },
                )
            }
            composable(route = NavigationRoute.Settings.destination) {
                SettingsScreenUI()
            }
            dialog(route = NavigationRoute.AddDialog.destination) { backStackEntry ->
                AddDialogUI(
                    date = (navigation.currentRouteParams as? Route.AddDialog.Params)?.date,
                    onDateClick = { date ->
                        navigation.openDatePickerDialog(date)
                    },
                    onButtonClick = {
                        navigation.back()
                    },
                )
            }
            dialog(route = NavigationRoute.DateDialog.destination) { backStackEntry ->
                DatePickerDialogUI(
                    date = (navigation.currentRouteParams as? Route.DateDialog.Params)?.date,
                    onButtonClick = { date ->
                        navigation.back(Route.DateDialog.Result(date))
                    },
                )
            }
        }
    }
}

@Composable
private fun Init(
    navigation: Navigation,
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        navigation.navigateToRoute.onEach { navigationRoute ->
            if (navigationRoute.destination == backStackEntry?.destination?.route) return@onEach

            navController.navigate(route = navigationRoute.destination) {
                launchSingleTop = true
                restoreState = true
            }
        }.launchIn(coroutineScope)

        navigation.navigateOnBack.onEach {
            navController.popBackStack()
        }.launchIn(coroutineScope)
    }
}
