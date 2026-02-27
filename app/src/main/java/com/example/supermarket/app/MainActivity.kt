package com.example.supermarket.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.presentation.screen.navigation.RootNavGraph
import com.example.supermarket.presentation.screen.navigation.Screens
import com.example.supermarket.presentation.ui.theme.SupermarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = sharedPref.getBoolean("is_onboarding_completed", false)

        val startDest = if (isOnboardingCompleted) Screens.MainFlow.route else "onboarding_screen"
        setContent {
            SupermarketTheme {
                val rootNavController = rememberNavController()
                RootNavGraph(
                    rootNavController = rootNavController,
                    startDestination = startDest
                )
            }
        }
    }
}