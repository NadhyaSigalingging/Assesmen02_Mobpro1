package com.nadhya0065.managementugas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.Form.route) {
            FormScreen(navController)
        }
        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetailScreen(navController, id)
        }
        composable(Screen.About.route) {
            AboutScreen(navController)
        }
        composable(Screen.RecycleBin.route) {
            RecycleBinScreen(navController)
        }
    }
}
