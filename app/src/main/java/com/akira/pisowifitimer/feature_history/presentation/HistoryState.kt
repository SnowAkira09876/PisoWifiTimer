package com.akira.pisowifitimer.feature_history.presentation

import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo

data class HistoryState(val history: List<TimeInfo> = emptyList())
