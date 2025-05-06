package com.nadhya0065.managementugas.navigation


sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object FormScreen : Screen("form_screen")
    object AboutScreen : Screen("about_screen")
}