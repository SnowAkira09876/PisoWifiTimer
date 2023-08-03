package com.akira.pisowifitimer.feature_history.data.repository

import com.akira.pisowifitimer.feature_history.data.local.HistoryInfoDao
import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

class HistoryRepositoryImpl(private val historyInfoDao: HistoryInfoDao) : HistoryRepository {
    override fun getHistory(): Flow<List<TimeInfo>> = historyInfoDao.getHistory()
    override fun getHistoryBetween(startDate: String, endDate: String): Flow<List<TimeInfo>> =
        historyInfoDao.getHistoryBetween(startDate, endDate)

    override suspend fun getTotalAmount(): Int = historyInfoDao.getTotalAmount()
    override suspend fun getTotalAmountBetween(startDate: String, endDate: String): Int =
        historyInfoDao.getTotalAmountBetween(startDate, endDate)

    override suspend fun getStartDate(): String = historyInfoDao.getStartDate()

    override suspend fun getEndDate(): String = historyInfoDao.getEndDate()

    override suspend fun getTotalTime(): Int = historyInfoDao.getTotalTime()
}