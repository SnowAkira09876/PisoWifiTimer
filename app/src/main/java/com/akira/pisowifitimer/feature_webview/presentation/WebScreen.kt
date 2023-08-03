package com.akira.pisowifitimer.feature_webview.presentation

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.webkit.WebViewClientCompat
import androidx.work.WorkManager
import com.akira.pisowifitimer.feature_timer.presentation.TimerBottomSheet
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    webClientCompat: WebViewClientCompat,
    chromeClient: WebChromeClient,
    workManager: WorkManager
) {
    val sheetState =
        rememberStandardBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState,
        snackbarHostState = snackbarHostState
    )
    val gateway = viewModel.wifi.value.gateway
    val isConnected = viewModel.wifi.value.isConnected

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    BottomSheetScaffold(scaffoldState = scaffoldState,
        sheetContent = {
            TimerBottomSheet(
                navController = navController,
                workManager = workManager,
                modifier = Modifier.padding(20.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isConnected) {
                AndroidView(factory = {
                    WebView(it).apply {
                        webViewClient = webClientCompat
                        webChromeClient = chromeClient
                        settings.javaScriptEnabled = true

                        loadUrl(gateway)
                    }
                })
            }
        }
    }
}