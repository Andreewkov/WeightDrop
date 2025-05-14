package ru.andreewkov.weightdrop.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.AppAction
import ru.andreewkov.weightdrop.ui.screen.Screen
import ru.andreewkov.weightdrop.ui.screen.add.AddDialogUI
import ru.andreewkov.weightdrop.ui.screen.history.HistoryScreenUI
import ru.andreewkov.weightdrop.ui.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.ui.util.ToolbarWidget
import ru.andreewkov.weightdrop.ui.util.ToolbarWidgetColors
import ru.andreewkov.weightdrop.ui.util.observe
import ru.andreewkov.weightdrop.ui.widget.NavigationBarColors
import ru.andreewkov.weightdrop.ui.widget.NavigationBarWidget

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainAppViewModel = hiltViewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }
    val showAddDialog by viewModel.showAddDialog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationScreen.observe { screen ->
            if (navController.currentDestination?.route != screen.id) {
                navController.navigate(screen.id)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ToolbarWidget(
                currentRoute = currentRoute,
                actionHandler = viewModel,
                colors = ToolbarWidgetColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
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
                isNavigationBarItemSelected = { item -> currentRoute == item.id }
            )
        }
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

            }
        }
    }

    if (showAddDialog) {
        AddDialogUI(
            actionHandler = viewModel,
        )
    }
}
