package com.nadhya0065.managementugas.navigation

import com.nadhya0065.managementugas.ui.screen.KEY_VAL_TUGAS

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("formTambahScreen")
    data object FormUbah: Screen("formUbahScreen/{$KEY_VAL_TUGAS}") {
        fun withId(id: Long) = "formUbahScreen/$id"
    }
}