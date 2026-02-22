package com.example.supermarket.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.presentation.screen.navigation.RootNavGraph
import com.example.supermarket.presentation.ui.theme.SupermarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SupermarketTheme {
                val rootNavController = rememberNavController()
                RootNavGraph(rootNavController = rootNavController)
            }
        }
    }
}