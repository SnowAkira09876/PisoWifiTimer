package com.akira.pisowifitimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.akira.pisowifitimer.core.util.Screen
import com.akira.pisowifitimer.feature_history.presentation.HistoryScreen
import com.akira.pisowifitimer.feature_webview.presentation.HomeScreen
import com.akira.pisowifitimer.feature_webview.util.AppChromeClient
import com.akira.pisowifitimer.feature_webview.util.AppWebClient
import com.akira.pisowifitimer.ui.theme.PisoWifiTimerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appChromeClient: AppChromeClient

    @Inject
    lateinit var appWebViewClientCompat: AppWebClient

    @Inject
    lateinit var workManager: WorkManager

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PisoWifiTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                navController = navController,
                                webClientCompat = appWebViewClientCompat,
                                chromeClient = appChromeClient,
                                workManager = workManager
                            )
                        }

                        composable(route = Screen.HistoryScreen.route) {
                            HistoryScreen()
                        }
                    }
                }
            }
        }
    }
}