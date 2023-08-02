package com.akira.pisowifitimer.feature_history.domain.use_case

import com.akira.pisowifitimer.feature_history.domain.repository.HistoryRepository
import com.akira.pisowifitimer.feature_timer.domain.model.TimeInfo
import kotlinx.coroutines.flow.Flow

class GetHistory(private val historyRepository: HistoryRepository) {

    operator fun invoke(): Flow<List<TimeInfo>> = historyRepository.getHistory()
}