package com.akira.pisowifitimer.core.util

sealed class Screen(val route: String) {
    data object HomeScreen: Screen("home_screen")
    data object HistoryScreen: Screen("history_screen")
}