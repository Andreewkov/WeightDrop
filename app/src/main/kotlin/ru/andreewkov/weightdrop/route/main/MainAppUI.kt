package ru.andreewkov.weightdrop.route.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.andreewkov.weightdrop.route.Route
import ru.andreewkov.weightdrop.route.dialog.add.AddDialogUI
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
    val state by viewModel.screenState.collectAsState()
    val addRequestState by viewModel.addRequestState.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        val id = currentBackStackEntry?.destination?.route ?: return@LaunchedEffect
        viewModel.onRouteIdUpdated(id = id)

        viewModel.navigateToRouteId.onEach { id ->
            navController.navigate(id)
        }.launchIn(this)

        viewModel.navigateOnBack.onEach {
            navController.popBackStack()
        }.launchIn(this)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarWidget(
                titleRes = state.toolbarTitleRes,
                colors = ToolbarWidgetColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
                onAddClick = viewModel::onAddClick,
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
                onBarScreenClick = { barScreen ->
                    viewModel.onBarScreenClick(barScreen)
                },
                isBarScreenSelected = { item -> state.selectedBarScreen.id == item.id },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.startScreen.id,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Route.InfoScreen.id) {
                InfoScreenUI()
            }
            composable(route = Route.HistoryScreen.id) {
                HistoryScreenUI(
                    onCardClick = { date ->
                        viewModel.onHistoryCardClick(date)
                    },
                )
            }
            composable(route = Route.SettingsScreen.id) {
                SettingsScreenUI()
            }
        }
    }

    when (val state = addRequestState) {
        is MainAppAddRequestState.Show -> {
            AddDialogUI(
                initialDate = state.date,
                onDismissRequest = viewModel::onAddDismissRequest,
            )
        }
        MainAppAddRequestState.Hide -> Unit
    }
}
