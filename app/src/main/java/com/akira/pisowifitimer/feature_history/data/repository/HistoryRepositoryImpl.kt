package com.akira.pisowifitimer.feature_history.data.repository

import com.akira.pisowifitimer.feature_history.data.local.HistoryInfoDao
import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(private val historyInfoDao: HistoryInfoDao) : HistoryRepository {
    override fun getHistory(): Flow<List<TimeInfo>> = historyInfoDao.getHistory()
}