package com.nadhya0065.managementugas.navigation


sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Form : Screen("form_screen")
    object Detail : Screen("detail_screen/{id}") {
        fun createRoute(id: Int) = "detail_screen/$id"
    }
    object About : Screen("about_screen")
    object RecycleBin : Screen("recycle_bin_screen")
}
