package com.akira.pisowifitimer.feature_webview.presentation

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.webkit.WebViewClientCompat
import androidx.work.WorkManager
import com.akira.pisowifitimer.feature_timer.presentation.TimerBottomSheet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    val snackbarHostState = remember { SnackbarHostState() }
    val gateway = viewModel.wifi.value.gateway
    val isConnected = viewModel.wifi.value.isConnected
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    LaunchedEffect(key1 = true) {
        viewModel.uiState.collectLatest { event ->
            when (event) {
                is UIState.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openBottomSheet = true },
            ) {
                Icon(Icons.Filled.Add, "Floating Action Button dismiss")
            }
        }
    ) { paddingValues ->
        if (isConnected) {
            AndroidView(
                modifier = Modifier.padding(paddingValues),
                factory = {
                    WebView(it).apply {
                        webViewClient = webClientCompat
                        webChromeClient = chromeClient
                        settings.javaScriptEnabled = true

                        loadUrl(gateway)
                    }
                })
        }
    }

    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            TimerBottomSheet(
                navController = navController,
                workManager = workManager,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .navigationBarsPadding()
            ) {
                scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        openBottomSheet = false
                    }
                }
            }
        }
    }
}