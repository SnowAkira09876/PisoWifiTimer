package com.akira.pisowifitimer.feature_timer.domain.repository

import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo

interface TimerRepository {
    suspend fun insertHistory(timeInfo: TimeInfo)

}