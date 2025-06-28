package ru.andreewkov.weightdrop.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.weightdrop.ui.screen.Screen
import ru.andreewkov.weightdrop.ui.screen.Screen.Companion.getStartScreen
import ru.andreewkov.weightdrop.ui.screen.add.AddScreenUI
import ru.andreewkov.weightdrop.ui.screen.history.HistoryScreenUI
import ru.andreewkov.weightdrop.ui.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.ui.screen.settings.SettingsScreenUI
import ru.andreewkov.weightdrop.ui.widget.NavigationBarColors
import ru.andreewkov.weightdrop.ui.widget.NavigationBarWidget
import ru.andreewkov.weightdrop.ui.widget.ToolbarWidget
import ru.andreewkov.weightdrop.ui.widget.ToolbarWidgetColors
import ru.andreewkov.weightdrop.util.observe

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: MainAppViewModel = hiltViewModel()
    var currentNavigationBarItem by remember { mutableStateOf(getStartScreen()) }

    LaunchedEffect(Unit) {
        viewModel.navigationScreen.observe { screen ->
            if (navController.currentDestination?.route != screen.id) {
                navController.navigate(screen.id)
                if (screen is Screen.NavigationBarItem) {
                    currentNavigationBarItem = screen
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarWidget(
                titleRes = currentNavigationBarItem.titleRes,
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
                items = Screen.getNavigationBarItems(),
                colors = NavigationBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    activeItemColor = MaterialTheme.colorScheme.secondary,
                    inactiveItemColor = MaterialTheme.colorScheme.primary,
                ),
                isNavigationBarItemSelected = { item -> currentNavigationBarItem.id == item.id },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = viewModel.getStartNavigationScreen().id,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Screen.Info.id) {
                InfoScreenUI()
            }
            composable(route = Screen.History.id) {
                HistoryScreenUI()
            }
            composable(route = Screen.Settings.id) {
                SettingsScreenUI()
            }
            dialog(route = Screen.Add.id) {
                AddScreenUI(navController = navController)
            }
        }
    }
}
