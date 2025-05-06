package com.nadhya0065.managementugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nadhya0065.managementugas.navigation.SetupNavGraph
import com.nadhya0065.managementugas.ui.theme.ManagemenTugasTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManagemenTugasTheme {
                SetupNavGraph()
            }
        }
    }
}

