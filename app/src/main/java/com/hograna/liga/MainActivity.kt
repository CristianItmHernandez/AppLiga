package com.hograna.liga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hograna.liga.ui.navigation.MainScaffoldWithFab
import com.hograna.liga.ui.theme.HogRanaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HogRanaTheme {
                MainScaffoldWithFab()
            }
        }
    }
}
