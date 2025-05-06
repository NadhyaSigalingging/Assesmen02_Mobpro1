package com.nadhya0065.managementugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nadhya0065.managementugas.database.TugasDatabase
import com.nadhya0065.managementugas.navigation.NavGraph
import com.nadhya0065.managementugas.ui.screen.MainViewModel
import com.nadhya0065.managementugas.ui.screen.MainViewModelFactory
import com.nadhya0065.managementugas.ui.theme.ManagemenTugasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TugasDatabase.getDatabase(applicationContext)
        val dao = database.tugasDao()
        val viewModelFactory = MainViewModelFactory(dao)

        setContent {
            ManagemenTugasTheme  {
                val navController = rememberNavController()
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                NavGraph(navController, viewModel)
            }
        }
    }
}
