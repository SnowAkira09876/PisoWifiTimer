package com.akira.pisowifitimer.feature_timer.data.repository

import com.akira.pisowifitimer.feature_timer.data.local.TimeInfoDao
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import com.akira.pisowifitimer.feature_timer.domain.repository.TimerRepository

class TimerRepositoryImpl(private val timeInfoDao: TimeInfoDao) : TimerRepository {
    override suspend fun insertHistory(timeInfo: TimeInfo) {
        timeInfoDao.insert(timeInfo)
    }
}