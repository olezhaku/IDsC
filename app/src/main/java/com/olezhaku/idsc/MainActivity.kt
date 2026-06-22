package com.olezhaku.idsc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.olezhaku.idsc.navigation.AppNavHost
import com.olezhaku.idsc.ui.screens.DevicesScreen
import com.olezhaku.idsc.ui.theme.IDsCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IDsCTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
