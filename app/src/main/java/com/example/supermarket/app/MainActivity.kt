package com.example.supermarket.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.supermarket.domain.usecase.user.UpdateFcmTokenUseCase
import com.example.supermarket.presentation.screen.navigation.RootNavGraph
import com.example.supermarket.presentation.screen.navigation.Screens
import com.example.supermarket.presentation.ui.theme.SupermarketTheme
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var updateFcmTokenUseCase: UpdateFcmTokenUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = getSharedPreferences("onboarding_prefs", MODE_PRIVATE)
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
        retrieveToken()
        Firebase.messaging.subscribeToTopic("all").addOnSuccessListener {
            println("Success topic")
        }
    }

    private fun retrieveToken() {
        val TAG = "FCM"
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(TAG, "Token $token")
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        updateFcmTokenUseCase(token)
                    } catch (e: Exception) {
                        Log.e(TAG, "Get token ${e.message}")
                    }
                }
            }
        }
    }
}