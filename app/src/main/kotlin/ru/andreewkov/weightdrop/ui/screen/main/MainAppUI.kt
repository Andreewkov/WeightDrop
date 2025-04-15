package ru.andreewkov.weightdrop.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.andreewkov.weightdrop.ui.screen.Screen
import ru.andreewkov.weightdrop.ui.screen.add.AddScreenUI
import ru.andreewkov.weightdrop.ui.screen.info.InfoScreenUI
import ru.andreewkov.weightdrop.ui.util.observe

@Composable
fun MainAppUI(
    navController: NavHostController = rememberNavController()
) {
    val viewModel: MainAppViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.navigationScreen.observe { screen ->
            navController.navigate(screen.name)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = viewModel.getStartNavigationScreen().name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Screen.Info.name) {
                InfoScreenUI()
            }
            composable(route = Screen.Add.name) {
                AddScreenUI()
            }
        }
    }
}