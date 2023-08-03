package com.akira.pisowifitimer.feature_history.presentation

sealed class HistoryEvent {
    data object TotalAmount : HistoryEvent()
    data object StartDate : HistoryEvent()
    data object EndDate : HistoryEvent()

}