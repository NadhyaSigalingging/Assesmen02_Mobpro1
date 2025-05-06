package com.nadhya0065.managementugas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.namamu.manajemengtugas.ui.screen.MainViewModel


@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {
            MainScreen(navController, viewModel)
        }
        composable(Screen.FormScreen.route) {
            FormScreen(navController, viewModel)
        }
        composable(Screen.AboutScreen.route) {
            AboutScreen(navController)
        }
    }
}
