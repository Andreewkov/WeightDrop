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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.weightdrop.R
import ru.andreewkov.weightdrop.ui.screen.Screen
import ru.andreewkov.weightdrop.ui.screen.add.AddScreenUI
import ru.andreewkov.weightdrop.ui.screen.history.HistoryScreenUI
import ru.andreewkov.weightdrop.ui.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.ui.theme.WeightDropTheme
import ru.andreewkov.weightdrop.ui.util.WeightDropPreview
import ru.andreewkov.weightdrop.ui.util.observe
import ru.andreewkov.weightdrop.ui.widget.NavigationBarColors
import ru.andreewkov.weightdrop.ui.widget.NavigationBarWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainAppViewModel = hiltViewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { backStackEntry?.destination?.route } }

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
            CenterAlignedTopAppBar(
                navigationIcon = {},
                title = {
                    Text(
                        text = stringResource(Screen.findScreen(currentRoute).titleRes),
                        style = TextStyle(
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.primary,
                ),
                windowInsets = WindowInsets.statusBars,
                actions = {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {  }
                            .padding(horizontal = 16.dp)
                            .size(20.dp),
                    )
                }
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
                isNavigationBarItemSelected = { item ->
                    currentRoute == item.id
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = viewModel.getStartNavigationScreen().id,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Screen.Info.id) {
                InfoScreenUI(
                    actionHandler = viewModel,
                )
            }
            composable(route = Screen.History.id) {
                HistoryScreenUI()
            }
            composable(route = Screen.Settings.id) {
                AddScreenUI()
            }
        }
    }
}
