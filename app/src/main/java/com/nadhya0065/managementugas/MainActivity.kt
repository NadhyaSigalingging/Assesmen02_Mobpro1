package com.nadhya0065.managementugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.nadhya0065.managementugas.navigation.NavGraph
import com.nadhya0065.managementugas.ui.theme.ManagemenTugasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManagemenTugasTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
