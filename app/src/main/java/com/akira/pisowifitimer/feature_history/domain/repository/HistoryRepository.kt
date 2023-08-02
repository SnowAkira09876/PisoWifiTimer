package com.akira.pisowifitimer.feature_history.domain.repository

import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(): Flow<List<TimeInfo>>
}