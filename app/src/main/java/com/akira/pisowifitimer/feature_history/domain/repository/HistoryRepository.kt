package com.akira.pisowifitimer.feature_history.domain.repository

import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistory(): Flow<List<TimeInfo>>

    fun getHistoryBetween(startDate: String, endDate: String): Flow<List<TimeInfo>>

    suspend fun getTotalAmount(): Int

    suspend fun getTotalAmountBetween(startDate: String, endDate: String): Int

    suspend fun getStartDate(): String

    suspend fun getEndDate(): String

    suspend fun getTotalTime(): Int
}